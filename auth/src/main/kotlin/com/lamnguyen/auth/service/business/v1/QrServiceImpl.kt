package com.lamnguyen.auth.service.business.v1

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.uuid.Generators
import com.lamnguyen.auth.domain.dto.ApiResponseSuccess
import com.lamnguyen.auth.domain.dto.RefreshTokenPayload
import com.lamnguyen.auth.exceptions.ApplicationException
import com.lamnguyen.auth.exceptions.ExceptionEnum
import com.lamnguyen.auth.service.business.IAuthService
import com.lamnguyen.auth.service.business.IQrService
import com.lamnguyen.auth.service.redis.v1.RefreshTokenCacheManager
import com.lamnguyen.auth.utils.enums.JwtTokenType
import com.lamnguyen.auth.utils.helpers.JwtHelper
import com.lamnguyen.auth.utils.properties.ApplicationProperty
import org.springframework.data.redis.core.ReactiveStringRedisTemplate
import org.springframework.http.ResponseCookie
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.Sinks
import reactor.core.publisher.Sinks.Many
import java.time.temporal.ChronoUnit
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap


/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 5:16 PM-30/09/2025
 *  User: kimin
 **/

@Service
class QrServiceImpl(
    val jwtHelper: JwtHelper,
    val redis: ReactiveStringRedisTemplate,
    val authService: IAuthService,
    val refreshTokenManager: RefreshTokenCacheManager,
    val jwtProperty: ApplicationProperty.Companion.AuthProperty.Companion.JwtProperty,
    val refreshTokenProperty: ApplicationProperty.Companion.AuthProperty.Companion.JwtProperty.Companion.RefreshTokenProperty,
) : IQrService {
    private val sinks: ConcurrentMap<String, Many<String>> = ConcurrentHashMap<String, Many<String>>()

    override fun createQrCodeLoginAndToken(): Mono<Map<String, String>> {
        val sid = Generators.timeBasedEpochRandomGenerator().generate().toString()
        val token = jwtHelper.createQrScanToken(sid).tokenValue

        return Mono.just(mapOf("sid" to sid, "token" to token))
    }

    override fun subscribe(token: String): Flux<String> {
        return jwtHelper.verifyToken(token)
            .map { it.subject }
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

    override fun confirm(tokenQrCode: String, refreshToken: String): Mono<Void> {
        return jwtHelper.verifyToken(tokenQrCode)
            .flatMap { jwt ->
                checkTokenInBlacklist(jwt.id)
                    .filter { !it }
                    .map { jwt }
            }
            .flatMap { jwt ->
                resign(refreshToken, jwt)
            }
    }

    private fun resign(refreshToken: String, jwt: Jwt): Mono<Void> {
        return checkRefreshToken(refreshToken)
            .flatMap { authService.resign(Mono.just(it)) }
            .flatMap { response ->
                addTokenInBlacklist(jwt.id)
                    .map { response }
            }
            .doOnSuccess {
                val sink: Many<String>? = sinks[jwt.subject]
                if (sink == null) return@doOnSuccess
                val cookie = ResponseCookie.from("REFRESH_TOKEN", it.refreshToken).apply {
                    maxAge(refreshTokenProperty.expires * 60000)
                    httpOnly(true)
                    secure(true)
                    path("/")
                }.build()
                val response = mapOf(
                    "phone_number" to it.phoneNumber,
                    "phone_number_code" to it.phoneNumberCode,
                    "access_token" to it.accessToken,
                    "refresh_token" to cookie.toString(),
                )

                sink.tryEmitNext(
                    ObjectMapper().writeValueAsString(
                        ApiResponseSuccess<Map<String, *>>()
                            .apply {
                                message = "Login Qr success"
                                data = response
                                code = 200
                            })
                )
                sink.tryEmitComplete()
            }
            .doOnError { ex ->
                val sink: Many<String>? = sinks[jwt.subject]
                if (sink == null) return@doOnError
                sink.tryEmitError(ex)
                sink.tryEmitComplete()
            }
            .then()

    }


    override fun checkRefreshToken(refreshToken: String): Mono<RefreshTokenPayload> {
        return jwtHelper
            .decodeAndVerifyJwt(refreshToken)
            .flatMap { jwt ->
                refreshTokenManager.existsTokenInBlackList(jwt.id)
                    .defaultIfEmpty(false)
                    .filter { !it }
                    .switchIfEmpty(Mono.error(ApplicationException(ExceptionEnum.RESIGN_FAILED)))
                    .map {
                        ObjectMapper().convertValue(
                            jwt.claims[jwtProperty.claimKey],
                            RefreshTokenPayload::class.java
                        )
                    }
            }.filter { it.type == JwtTokenType.REFRESH }
            .switchIfEmpty(Mono.error(ApplicationException(ExceptionEnum.MISSING_REFRESH_TOKEN)))
    }

    private fun checkTokenInBlacklist(id: String): Mono<Boolean> {
        return redis.opsForValue()
            .get("blacklist:qr-login-$id")
            .map { true }
            .switchIfEmpty(Mono.just(false))
    }

    private fun addTokenInBlacklist(id: String): Mono<Boolean> {
        return redis.opsForValue()
            .set("blacklist:qr-login-$id", "", java.time.Duration.of(1, ChronoUnit.MINUTES))
    }
}