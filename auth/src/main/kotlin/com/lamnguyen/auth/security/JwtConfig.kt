package com.lamnguyen.auth.security

import com.lamnguyen.auth.utils.properties.ApplicationProperty
import com.nimbusds.jose.jwk.source.ImmutableSecret
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.jose.jws.MacAlgorithm
import org.springframework.security.oauth2.jwt.*
import javax.crypto.spec.SecretKeySpec

/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 2:55 PM-08/07/2025
 *  User: kimin
 **/

@Configuration
class JwtConfig(val jwtProperty: ApplicationProperty.Companion.AuthProperty.Companion.JwtProperty) {
    @Bean
    fun jwsHeader(): JwsHeader {
        return JwsHeader.with(MacAlgorithm.HS256).type("JWT").build()
    }

    @Bean
    fun decoder(): ReactiveJwtDecoder {
        return NimbusReactiveJwtDecoder.withSecretKey(
            SecretKeySpec(
                jwtProperty.secretKey.toByteArray(Charsets.UTF_8),
                MacAlgorithm.HS256.name
            )
        ).build()
    }

    @Bean
    fun encoder(): JwtEncoder {
        return NimbusJwtEncoder(ImmutableSecret(jwtProperty.secretKey.toByteArray(Charsets.UTF_8)))
    }
}