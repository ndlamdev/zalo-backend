/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 7:43 PM-12/07/2025
 *  User: kimin
 **/

package com.lamnguyen.auth.utils.helpers

import com.fasterxml.jackson.databind.ObjectMapper
import com.lamnguyen.auth.domain.dto.AccessTokenPayload
import com.lamnguyen.auth.domain.dto.RefreshTokenPayload
import com.lamnguyen.auth.model.User
import com.lamnguyen.auth.utils.properties.ApplicationProperty
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.jwt.*
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit

@Component
class JwtHelper(
    private val jwtEncoder: JwtEncoder,
    private val jwtDecode: ReactiveJwtDecoder,
    private val jwsHeader: JwsHeader,
    private val jwtProperty: ApplicationProperty.Companion.AuthProperty.Companion.JwtProperty,
    private val accessTokenProperty: ApplicationProperty.Companion.AuthProperty.Companion.JwtProperty.Companion.AccessTokenProperty
) {
    fun createAccessToken(id: String, auth: Authentication, refreshTokenId: String): Jwt {
        val now = LocalDateTime.now().toInstant(ZoneOffset.UTC)
        return jwtEncoder.encode(
            JwtEncoderParameters.from(
                jwsHeader, JwtClaimsSet.builder()
                    .id(id)
                    .issuer(jwtProperty.iss)
                    .subject(auth.name)
                    .issuedAt(now)
                    .claim(jwtProperty.claimKey, AccessTokenPayload.generateToken(auth, refreshTokenId))
                    .expiresAt(now.plus(accessTokenProperty.expires, ChronoUnit.MINUTES))
                    .build()
            )
        )
    }

    fun createAccessToken(id: String, user: User, roles: List<String>, refreshTokenId: String): Jwt {
        val now = LocalDateTime.now().toInstant(ZoneOffset.UTC)
        return jwtEncoder.encode(
            JwtEncoderParameters.from(
                jwsHeader, JwtClaimsSet.builder()
                    .id(id)
                    .issuer(jwtProperty.iss)
                    .subject(user.phoneNumber)
                    .issuedAt(now)
                    .claim(
                        jwtProperty.claimKey,
                        AccessTokenPayload.generateToken(user.phoneNumber, roles, refreshTokenId)
                    )
                    .expiresAt(now.plus(accessTokenProperty.expires, ChronoUnit.MINUTES))
                    .build()
            )
        )
    }

    fun createRefreshToken(id: String, phoneNumber: String, accessTokenId: String): Jwt {
        val now = LocalDateTime.now().toInstant(ZoneOffset.UTC)
        return jwtEncoder.encode(
            JwtEncoderParameters.from(
                jwsHeader, JwtClaimsSet.builder()
                    .id(id)
                    .issuer(jwtProperty.iss)
                    .subject(phoneNumber)
                    .issuedAt(now)
                    .claim(jwtProperty.claimKey, RefreshTokenPayload.generateToken(phoneNumber, accessTokenId))
                    .expiresAt(now.plus(accessTokenProperty.expires, ChronoUnit.MINUTES))
                    .build()
            )
        )
    }

    fun decodeAndVerifyJwt(jwt: String): Mono<Jwt> {
        return jwtDecode.decode(jwt)
    }

    fun getRefreshTokenPayload(jwt: String): Mono<RefreshTokenPayload> {
        return jwtDecode.decode(jwt)
            .map {
                ObjectMapper().convertValue(it.claims[jwt], RefreshTokenPayload::class.java)
            }
    }
}