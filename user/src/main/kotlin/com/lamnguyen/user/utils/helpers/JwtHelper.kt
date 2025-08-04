/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 6:05 PM-30/07/2025
 *  User: kimin
 **/

package com.lamnguyen.user.utils.helpers

import com.fasterxml.jackson.databind.ObjectMapper
import com.lamnguyen.user.domain.dto.AccessTokenPayload
import com.lamnguyen.user.utils.properties.ApplicationProperty
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Component
import java.security.interfaces.RSAPublicKey

@Component
class JwtHelper(
    val jwtProperty: ApplicationProperty.Companion.AuthProperty.Companion.JwtProperty,
    val publicKey: RSAPublicKey
) {
    fun decodeToken(token: String): Jwt {
        return NimbusJwtDecoder
            .withPublicKey(publicKey)
            .build()
            .decode(token)
    }

    fun initJwtAuthenticationToken(
        token: String,
        authorities: MutableSet<SimpleGrantedAuthority>
    ): JwtAuthenticationToken {
        val jwt = decodeToken(token)

        val payload = ObjectMapper()
            .convertValue(
                jwt.claims[jwtProperty.claimKey],
                AccessTokenPayload::class.java
            )

        payload.roles?.forEach { role ->
            authorities.add(SimpleGrantedAuthority(role))
        }


        return JwtAuthenticationToken(
            jwt,
            authorities
        )
    }
}