package com.lamnguyen.auth.service.business.v1

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.uuid.Generators
import com.lamnguyen.auth.domain.dto.ApiResponseSuccess
import com.lamnguyen.auth.domain.responses.TokenResponse
import com.lamnguyen.auth.exceptions.ApplicationException
import com.lamnguyen.auth.exceptions.ExceptionEnum
import com.lamnguyen.auth.service.business.IQrService
import com.lamnguyen.auth.utils.enums.LoginStatus
import com.lamnguyen.auth.utils.helpers.JwtHelper
import org.springframework.data.redis.core.ReactiveStringRedisTemplate
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.stereotype.Service
import parsePhoneNumber
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.Sinks
import reactor.core.publisher.Sinks.Many
import reactor.core.scheduler.Schedulers
import reactor.kotlin.core.util.function.component1
import reactor.kotlin.core.util.function.component2
import java.time.Duration
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap


/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 5:16 PM-30/09/2025
 *  User: kimin
 **/

/**
 * Triển khai nghiệp vụ đăng nhập bằng QR code.
 *
 * Service dùng JWT để tạo token tạm thời, Redis để lưu trạng thái/blacklist token
 * và Sinks để đẩy kết quả đăng nhập realtime cho client đang chờ xác nhận.
 */
@Service
class QrServiceImpl(
    val jwtHelper: JwtHelper,
    val redis: ReactiveStringRedisTemplate,
    val userDetailService: ReactiveUserDetailsServiceImpl,
    val objectMapper: ObjectMapper,
) : IQrService {
    private val sinks: ConcurrentMap<String, Many<String>> = ConcurrentHashMap()

    /**
     * Tạo một QR scan token để client dùng render QR login.
     */
    override fun createQrCodeLoginAndToken(): Mono<Map<String, String>> {
        val sid = Generators.timeBasedEpochRandomGenerator().generate().toString()
        val token = jwtHelper.createQrScanToken(sid)

        return updateLoginStatus(token.id)
            .thenReturn(mapOf("sid" to sid, "token" to token.tokenValue))
    }

    /**
     * Client dùng token đã tạo để đăng ký channel nhận kết quả QR login.
     */
    override fun subscribe(qrScanToken: String): Flux<String> {
        return jwtHelper.verifyToken(qrScanToken)
            .filterWhen { notExitQrTokenInBlacklist(it.id) }
            .switchIfEmpty(Mono.error(ApplicationException(ExceptionEnum.INVALID_TOKEN)))
            .flatMap { getLoginStatus(it.id).zipWith(Mono.just(it)) }
            .filter { (status, _) -> status != null && status == LoginStatus.INITIAL.name }
            .switchIfEmpty(Mono.error(ApplicationException(ExceptionEnum.INVALID_LOGIN_STATUS)))
            .flatMap { (_, jwt) ->
                updateLoginStatus(jwt.id, LoginStatus.WAITING_SCAN)
                    .map { jwt.id }
            }
            .flatMapMany {
                val sink = sinks.computeIfAbsent(it) {
                    Sinks.many().multicast().onBackpressureBuffer()
                }

                sink.asFlux()
                    .doFinally { type ->
                        // Khi client hủy kết nối (cancel, error, complete)
                        println("Connection closed for sid=$it, reason=$type")
                        // cleanup tránh memory leak
                        sinks.remove(it)
                    }
            }
    }

    /**
     * Xử lý khi một client khác scan QR để bắt đầu quá trình xác thực đăng nhập.
     */
    override fun scan(qrScanToken: String): Mono<Void> {
        return jwtHelper.verifyToken(qrScanToken)
            .filterWhen { notExitQrTokenInBlacklist(it.id) }
            .switchIfEmpty(Mono.error(ApplicationException(ExceptionEnum.INVALID_TOKEN)))
            .filterWhen {
                getLoginStatus(it.id)
                    .map { status -> status != null && status == LoginStatus.WAITING_SCAN.name }
            }
            .switchIfEmpty(Mono.error(ApplicationException(ExceptionEnum.INVALID_LOGIN_STATUS)))
            .flatMap { jwt ->
                updateLoginStatus(jwt.id, LoginStatus.WAITING_CONFIRM)
                    .thenReturn(jwt.id)
            }.doOnNext {
                val result = ApiResponseSuccess<Any>().apply {
                    message = "Scan qr login success! Please waiting confirm!"
                }

                sinks[it]?.tryEmitNext(objectMapper.writeValueAsString(result))
            }
            .then(Mono.empty())
    }

    /**
     * Xác nhận hoặc hủy phiên QR login sau khi QR đã được scan.
     */
    override fun confirm(qrScanToken: String, status: LoginStatus): Mono<Void> {
        if (status != LoginStatus.CONFIRM && status != LoginStatus.CANCELED) {
            return Mono.error(ApplicationException(ExceptionEnum.INVALID_LOGIN_STATUS))
        }

        return Mono.zip(jwtHelper.verifyToken(qrScanToken), ReactiveSecurityContextHolder.getContext())
            .onErrorMap { ApplicationException(ExceptionEnum.INVALID_TOKEN) }
            .filterWhen { (qr, _) ->
                notExitQrTokenInBlacklist(qr.id)
            }
            .switchIfEmpty(Mono.error(ApplicationException(ExceptionEnum.INVALID_TOKEN)))
            .filterWhen { (qr, _) ->
                getLoginStatus(qr.id)
                    .map { status -> status != null && status == LoginStatus.WAITING_CONFIRM.name }
            }
            .switchIfEmpty(Mono.error(ApplicationException(ExceptionEnum.INVALID_LOGIN_STATUS)))
            .map { (qr, context) ->
                val sink = sinks.getOrDefault(qr.id, null)

                if (status == LoginStatus.CONFIRM) {
                    val loginToken = jwtHelper.createQrLoginToken(context.authentication.name as String, qr.id)
                    val result = ApiResponseSuccess<Any>().apply {
                        message = "Confirm qr login success!"
                        data = mapOf("token" to loginToken.tokenValue)
                    }

                    sink.tryEmitNext(objectMapper.writeValueAsString(result))
                }

                sink.tryEmitComplete()
                qr.id
            }.flatMap {
                Mono.zip(addQrTokenInBlacklist(it), updateLoginStatus(it, status))
            }.then(Mono.empty())
    }

    /**
     * Dùng login token đã được xác nhận để tạo access token và refresh token.
     */
    override fun login(loginToken: String?): Mono<TokenResponse> {
        if (loginToken == null) {
            throw ApplicationException(ExceptionEnum.MISSING_ACCESS_TOKEN)
        }

        return jwtHelper.decodeAndVerifyJwt(loginToken)
            .filterWhen {
                notExitQrLoginTokenInBlacklist(it.id)
            }
            .switchIfEmpty(Mono.error(ApplicationException(ExceptionEnum.INVALID_TOKEN)))
            .filterWhen {
                getLoginStatus(it.claims["qr-token-id"] as String)
                    .map { status -> status != null && status == LoginStatus.CONFIRM.name }
            }
            .switchIfEmpty(Mono.error(ApplicationException(ExceptionEnum.INVALID_LOGIN_STATUS)))
            .flatMap {
                userDetailService.findByUsername(it.subject).zipWith(Mono.just(it.id))
            }
            .publishOn(Schedulers.boundedElastic())
            .flatMap { (userDetail, qrTokenId) ->
                val phoneNumber = parsePhoneNumber(userDetail.username)

                val accessTokenId = UUID.randomUUID().toString()
                val refreshTokenId = UUID.randomUUID().toString()
                val refreshToken = jwtHelper.createRefreshToken(refreshTokenId, userDetail.username, accessTokenId)
                val accessToken = jwtHelper.createAccessToken(accessTokenId, userDetail, refreshTokenId)


                Mono.zip(
                    Mono.just(
                        TokenResponse(
                            phoneNumber.nationalNumber,
                            phoneNumber.countryCode,
                            accessToken.tokenValue,
                            refreshToken.tokenValue
                        )
                    ), Mono.just(qrTokenId)
                )
            }.flatMap { (tokenResponse, qrTokenId) ->
                addQrLoginTokenInBlacklist(qrTokenId)
                    .map { tokenResponse }
            }
    }

    /**
     * Kiểm tra QR scan token chưa nằm trong blacklist.
     */
    private fun notExitQrTokenInBlacklist(id: String): Mono<Boolean> {
        return redis.opsForValue()
            .get("blacklist:qr-scan-token:$id")
            .map { false }
            .switchIfEmpty(Mono.just(true))
    }

    /**
     * Đưa QR scan token vào blacklist sau khi phiên được xác nhận hoặc hủy.
     */
    private fun addQrTokenInBlacklist(id: String): Mono<Boolean> {
        return redis.opsForValue()
            .set("blacklist:qr-scan-token:$id", "", Duration.of(1, ChronoUnit.MINUTES))
    }

    /**
     * Kiểm tra QR login token chưa nằm trong blacklist.
     */
    private fun notExitQrLoginTokenInBlacklist(id: String): Mono<Boolean> {
        return redis.opsForValue()
            .get("blacklist:qr-login-token:$id")
            .map { false }
            .switchIfEmpty(Mono.just(true))
    }

    /**
     * Đưa QR login token vào blacklist sau khi token đã được dùng để đăng nhập.
     */
    private fun addQrLoginTokenInBlacklist(id: String): Mono<Boolean> {
        return redis.opsForValue()
            .set("blacklist:qr-login-token:$id", "", Duration.of(1, ChronoUnit.MINUTES))
    }

    /**
     * Cập nhật trạng thái hiện tại của phiên QR login trong Redis.
     */
    private fun updateLoginStatus(id: String, status: LoginStatus = LoginStatus.INITIAL): Mono<Boolean> {
        return redis.opsForValue()
            .set("qr-token:login:$id", status.name, Duration.of(2, ChronoUnit.MINUTES))
    }

    /**
     * Lấy trạng thái hiện tại của phiên QR login từ Redis.
     */
    private fun getLoginStatus(id: String): Mono<String> {
        return redis.opsForValue().get("qr-token:login:$id")
    }
}
