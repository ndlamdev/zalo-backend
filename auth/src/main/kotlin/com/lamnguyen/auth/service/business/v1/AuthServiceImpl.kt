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
import com.lamnguyen.auth.model.User
import com.lamnguyen.auth.repositories.IRoleRepository
import com.lamnguyen.auth.repositories.IUserRepository
import com.lamnguyen.auth.service.business.IAuthService
import com.lamnguyen.auth.service.kafka.IUserKafkaService
import com.lamnguyen.auth.service.redis.v1.AccessTokenCacheManager
import com.lamnguyen.auth.service.redis.v1.RefreshTokenCacheManager
import com.lamnguyen.auth.utils.enums.JwtTokenType
import com.lamnguyen.auth.utils.helpers.JwtHelper
import com.lamnguyen.auth.utils.properties.ApplicationProperty
import formatPhoneNumber
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import parsePhoneNumber
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import reactor.kotlin.core.publisher.switchIfEmpty
import java.util.*

@Service
class AuthServiceImpl(
    val userRepository: IUserRepository,
    val roleRepository: IRoleRepository,
    val passwordEncoder: PasswordEncoder,
    val userKafkaService: IUserKafkaService,
    val jwtHelper: JwtHelper,
    val accessTokenManager: AccessTokenCacheManager,
    val refreshTokenManager: RefreshTokenCacheManager,
    val jwtProperty: ApplicationProperty.Companion.AuthProperty.Companion.JwtProperty,
) : IAuthService {
    override fun register(data: RegisterRequest): Mono<Void> {
        val phoneNumber = formatPhoneNumber(data.phoneNumber)
        return userRepository.findByPhoneNumber(phoneNumber)
            .flatMap {
                Mono.error<Void>(ApplicationException(ExceptionEnum.USER_EXISTED))
            }.switchIfEmpty {
                userRepository.save(User().apply {
                    this.phoneNumber = phoneNumber
                    this.password = passwordEncoder.encode(data.password)
                })
                    .flatMap { userKafkaService.createUser(phoneNumber) }
                    .then()
            }
    }

    override fun hasPhoneNumber(phoneNumber: String?): Mono<Void> {
        val phoneNumberFormated = formatPhoneNumber(phoneNumber)
        return userRepository.existsUserByPhoneNumber(phoneNumberFormated)
            .filter { it }
            .switchIfEmpty(Mono.error(ApplicationException(ExceptionEnum.USER_NOT_EXISTS)))
            .then()
    }

    override fun resign(refreshToken: String): Mono<TokenResponse> {
        return resign(checkRefreshTokenAndAddIntoBlacklist(refreshToken))
    }

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




    private fun createToken(accessTokenId: String, phoneNumberFormated: String, refreshTokenId: String): Mono<String> {
        val monoUser = userRepository.findByPhoneNumber(phoneNumberFormated)
        val monoRoles = roleRepository.findByUserPhoneNumber(phoneNumberFormated)
        return Mono.zip(monoUser, monoRoles.collectList())
            .mapNotNull {
                val user = it.t1
                val roles = it.t2

                if (user == null || roles == null)
                    throw ApplicationException(ExceptionEnum.USER_NOT_EXISTS)

                return@mapNotNull jwtHelper.createAccessToken(
                    accessTokenId,
                    user,
                    roles.stream().map { role -> role!!.name }.toList(),
                    refreshTokenId
                ).tokenValue
            }
    }

    override fun logout(refreshToken: String): Mono<Void> {
        return checkRefreshTokenAndAddIntoBlacklist(refreshToken)
            .flatMap {
                accessTokenManager.saveTokenInBlackList(it.accessTokenId!!)
            }
            .then()
    }

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
}