/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 6:33 AM-10/07/2025
 *  User: kimin
 **/

package com.lamnguyen.auth.service.business.v1

import com.fasterxml.jackson.databind.ObjectMapper
import com.lamnguyen.auth.domain.dto.RefreshTokenPayload
import com.lamnguyen.auth.domain.requests.RegisterRequest
import com.lamnguyen.auth.domain.responses.TokenResponse
import com.lamnguyen.auth.exceptions.ApplicationException
import com.lamnguyen.auth.exceptions.ExceptionEnum
import com.lamnguyen.auth.model.RolesOfUser
import com.lamnguyen.auth.model.User
import com.lamnguyen.auth.repositories.IRoleRepository
import com.lamnguyen.auth.repositories.IRolesOfUserRepository
import com.lamnguyen.auth.repositories.IUserRepository
import com.lamnguyen.auth.service.business.IAuthService
import com.lamnguyen.auth.service.kafka.IUserKafkaService
import com.lamnguyen.auth.service.kafka.SmsSenderKafkaServiceImpl
import com.lamnguyen.auth.service.redis.v1.AccessTokenCacheManager
import com.lamnguyen.auth.service.redis.v1.OtpCacheManager
import com.lamnguyen.auth.service.redis.v1.RefreshTokenCacheManager
import com.lamnguyen.auth.utils.enums.BaseRole
import com.lamnguyen.auth.utils.enums.JwtTokenType
import com.lamnguyen.auth.utils.helpers.JwtHelper
import com.lamnguyen.auth.utils.helpers.generateOtp
import com.lamnguyen.auth.utils.properties.ApplicationProperty
import formatPhoneNumber
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import parsePhoneNumber
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import reactor.kotlin.core.util.function.component1
import reactor.kotlin.core.util.function.component2
import java.util.*

/**
 * Triển khai nghiệp vụ xác thực dựa trên repository, Redis cache, JWT và Kafka.
 *
 * Class này chịu trách nhiệm đăng ký tài khoản, kiểm tra số điện thoại,
 * tạo mới token, thu hồi token khi đăng xuất hoặc resign và gửi OTP xác thực.
 */
@Service
class AuthServiceImpl(
    val userRepository: IUserRepository,
    val roleRepository: IRoleRepository,
    val rolesOfUserRepository: IRolesOfUserRepository,
    val passwordEncoder: PasswordEncoder,
    val userKafkaService: IUserKafkaService,
    val jwtHelper: JwtHelper,
    val accessTokenManager: AccessTokenCacheManager,
    val refreshTokenManager: RefreshTokenCacheManager,
    val otpCacheManager: OtpCacheManager,
    val jwtProperty: ApplicationProperty.Companion.AuthProperty.Companion.JwtProperty,
    val otpSenderKafkaService: SmsSenderKafkaServiceImpl
) : IAuthService {
    /**
     * Đăng ký tài khoản bằng số điện thoại và mật khẩu.
     *
     * Số điện thoại được chuẩn hóa trước khi kiểm tra trùng lặp. Khi đăng ký thành công,
     * service tạo user nội bộ, gửi sự kiện tạo user qua Kafka và lưu trạng thái tạo tài khoản vào Redis.
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun register(data: RegisterRequest): Mono<Void> {
        val phoneNumber = formatPhoneNumber(data.phoneNumber)
        return userRepository.findByPhoneNumber(phoneNumber)
            .flatMap {
                Mono.error<Void>(ApplicationException(ExceptionEnum.USER_EXISTED))
            }.switchIfEmpty {
                userRepository.save(User().apply {
                    this.phoneNumber = phoneNumber
                    this.password = passwordEncoder.encode(data.password)
                    this.active = false
                })
                    .then(
                        rolesOfUserRepository.save(RolesOfUser().apply {
                            this.userPhoneNumber = phoneNumber
                            this.roleName = BaseRole.USER.name
                        })
                    ).then()
            }.then(
                Mono.zip(
                    userKafkaService.createUser(phoneNumber),
                    otpCacheManager.startSessionValidateAccount(phoneNumber)
                )
            ).then()
            .doOnError { println(it) }
            .onErrorResume { Mono.error(ApplicationException(ExceptionEnum.REGISTER_ERROR)) }
    }

    /**
     * Kiểm tra số điện thoại đã tồn tại sau khi chuẩn hóa định dạng.
     */
    override fun existPhoneNumber(phoneNumber: String?): Mono<Boolean> {
        val phoneNumberFormated = formatPhoneNumber(phoneNumber)
        return userRepository.existsUserByPhoneNumber(phoneNumberFormated)
            .switchIfEmpty(Mono.error(ApplicationException(ExceptionEnum.USER_NOT_EXISTS)))
    }

    /**
     * Xác thực refresh token, đưa token cũ vào blacklist rồi cấp cặp token mới.
     */
    override fun resign(refreshToken: String): Mono<TokenResponse> {
        return resign(checkRefreshTokenAndAddIntoBlacklist(refreshToken))
    }

    /**
     * Tạo access token và refresh token mới từ payload refresh token hợp lệ.
     */
    override fun resign(refreshToken: Mono<RefreshTokenPayload>): Mono<TokenResponse> {
        return refreshToken
            .flatMap { payload ->
                val phoneNumberFormated = formatPhoneNumber(payload.phoneNumber)
                val phoneNumber = parsePhoneNumber(phoneNumberFormated)
                val accessTokenId = UUID.randomUUID().toString()
                val refreshTokenId = UUID.randomUUID().toString()
                val monoAccessToken = createToken(accessTokenId, phoneNumberFormated, refreshTokenId)
                val newRefreshToken = jwtHelper.createRefreshToken(refreshTokenId, phoneNumberFormated, accessTokenId)
                return@flatMap monoAccessToken
                    .map {
                        TokenResponse(
                            phoneNumber.nationalNumber,
                            phoneNumber.countryCode,
                            it,
                            newRefreshToken.tokenValue
                        )
                    }
            }
            .onErrorResume { Mono.error(it) }
    }

    /**
     * Gửi OTP cho số điện thoại đã hoàn tất bước đăng ký.
     *
     * Service chặn gửi OTP nếu tài khoản chưa đăng ký thành công hoặc OTP đã được gửi trước đó.
     */
    override fun sentOtp(phoneNumber: String): Mono<Void> {
        return Mono.zip(
            otpCacheManager.isWaitingValidateAccount(phoneNumber)
                .switchIfEmpty {
                    userRepository.existsUserByPhoneNumberAndActiveIsFalse(phoneNumber)
                        .filter { it }
                        .flatMap { exist ->
                            otpCacheManager.startSessionValidateAccount(phoneNumber)
                                .map { exist }
                        }.switchIfEmpty(Mono.just(false))
                },
            otpCacheManager.isSentOtp(phoneNumber)
        )
            .flatMap { (isRegisterSuccess, isSentOtp) ->
                if (!isRegisterSuccess) return@flatMap Mono.error(ApplicationException(ExceptionEnum.INVALID_PHONE_NUMBER))
                if (isSentOtp) return@flatMap Mono.error(ApplicationException(ExceptionEnum.OTP_SEND))


                val otp = generateOtp(6)
                return@flatMap Mono.zip(
                    otpCacheManager.saveOtp(phoneNumber, otp),
                    otpCacheManager.resetTimeRegisterSuccess(phoneNumber),
                    otpSenderKafkaService.sendOtp(phoneNumber, otp).thenReturn(true)
                ).then(Mono.empty())
            }
    }


    /**
     * Tạo access token từ thông tin user và danh sách role hiện tại.
     */
    private fun createToken(
        accessTokenId: String,
        phoneNumberFormated: String,
        refreshTokenId: String
    ): Mono<String> {
        val monoUser = userRepository.findByPhoneNumber(phoneNumberFormated)
        val monoRoles = roleRepository.findByUserPhoneNumber(phoneNumberFormated)
        return Mono.zip(monoUser, monoRoles.collectList())
            .map {
                val user = it.t1
                val roles = it.t2

                jwtHelper.createAccessToken(
                    accessTokenId,
                    user,
                    roles.stream().map { role -> role.name }.toList(),
                    refreshTokenId
                ).tokenValue
            }
    }

    /**
     * Đăng xuất bằng refresh token và thu hồi access token đang liên kết với refresh token đó.
     */
    override fun logout(refreshToken: String): Mono<Void> {
        return checkRefreshTokenAndAddIntoBlacklist(refreshToken)
            .flatMap {
                accessTokenManager.saveTokenInBlackList(it.accessTokenId!!)
            }
            .then()
    }

    /**
     * Kiểm tra refresh token hợp lệ, chưa nằm trong blacklist và đúng loại REFRESH.
     *
     * Khi hợp lệ, refresh token và access token liên quan sẽ được lưu vào blacklist để tránh tái sử dụng.
     */
    private fun checkRefreshTokenAndAddIntoBlacklist(refreshToken: String): Mono<RefreshTokenPayload> {
        return jwtHelper
            .decodeAndVerifyJwt(refreshToken)
            .flatMap { jwt ->
                refreshTokenManager.existsTokenInBlackList(jwt.id)
                    .defaultIfEmpty(false)
                    .filter { !it }
                    .switchIfEmpty(Mono.error(ApplicationException(ExceptionEnum.RESIGN_FAILED)))
                    .flatMap {
                        val payload = ObjectMapper().convertValue(
                            jwt.claims[jwtProperty.claimKey],
                            RefreshTokenPayload::class.java
                        )
                        Mono.zip(
                            refreshTokenManager.saveTokenInBlackList(jwt.id),
                            accessTokenManager.saveTokenInBlackList(payload.accessTokenId ?: ""),
                        ).then(Mono.just(payload))
                    }
            }.filter { it.type == JwtTokenType.REFRESH }
            .switchIfEmpty(Mono.error(ApplicationException(ExceptionEnum.MISSING_REFRESH_TOKEN)))
    }

    /**
     * Xác thực OTP đăng ký tài khoản cho số điện thoại.
     *
     * Khi OTP hợp lệ, tài khoản được kích hoạt và session OTP/trạng thái đăng ký
     * trong cache sẽ được xóa.
     */
    override fun validateAccount(
        phoneNumber: String,
        otp: String
    ): Mono<Void> {
        return otpCacheManager.getOtp(phoneNumber)
            .switchIfEmpty(Mono.error(ApplicationException(ExceptionEnum.OTP_EXPIRED)))
            .filter { otp == it }
            .switchIfEmpty(Mono.error(ApplicationException(ExceptionEnum.INVALID_OTP)))
            .flatMap {
                userRepository.activeUserByPhoneNumber(phoneNumber)
            }
            .then(otpCacheManager.clearSessionValidateAccount(phoneNumber))
    }
}
