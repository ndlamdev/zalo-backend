/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 7:43 PM-12/07/2025
 *  User: kimin
 **/

package com.lamnguyen.auth.utils.helpers

import com.lamnguyen.auth.domain.dto.JWTPayload
import com.lamnguyen.auth.domain.dto.SimplePayload
import com.lamnguyen.auth.utils.enums.JwtTokenType
import com.lamnguyen.auth.utils.properties.ApplicationProperty
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.jwt.JwsHeader
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit
import java.util.UUID

@Component
class JwtHelper(
    private val jwtEncoder: JwtEncoder,
    private val jwsHeader: JwsHeader,
    private val jwtProperty: ApplicationProperty.Companion.Auth.Companion.Jwt,
    private val accessTokenProperty: ApplicationProperty.Companion.Auth.Companion.Jwt.Companion.AccessToken
) {
    fun createAccessToken(auth: Authentication, refreshTokenId: String): Jwt {
        val now = LocalDateTime.now().toInstant(ZoneOffset.UTC);
        return jwtEncoder.encode(
            JwtEncoderParameters.from(
                jwsHeader, JwtClaimsSet.builder()
                    .id(UUID.randomUUID().toString())
                    .issuer(jwtProperty.iss)
                    .subject(auth.name)
                    .issuedAt(now)
                    .claim(jwtProperty.claimKey, JWTPayload.generateForAccessToken(auth, refreshTokenId))
                    .expiresAt(now.plus(accessTokenProperty.expires, ChronoUnit.MINUTES))
                    .build()
            )
        );
    }

    fun createRefreshToken(auth: Authentication): Jwt {
        val now = LocalDateTime.now().toInstant(ZoneOffset.UTC);
        return jwtEncoder.encode(
            JwtEncoderParameters.from(
                jwsHeader, JwtClaimsSet.builder()
                    .id(UUID.randomUUID().toString())
                    .issuer(jwtProperty.iss)
                    .subject(auth.name)
                    .issuedAt(now)
                    .claim(jwtProperty.claimKey, SimplePayload().apply {
                        phoneNumber = auth.name
                        type = JwtTokenType.REFRESH
                    })
                    .expiresAt(now.plus(accessTokenProperty.expires, ChronoUnit.MINUTES))
                    .build()
            )
        );
    }
}