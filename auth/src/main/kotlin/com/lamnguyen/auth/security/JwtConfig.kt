package com.lamnguyen.auth.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.jose.jws.MacAlgorithm
import org.springframework.security.oauth2.jwt.JwsHeader
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder
import javax.crypto.spec.SecretKeySpec

/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 2:55 PM-08/07/2025
 *  User: kimin
 **/

@Configuration
class JwtConfig {
    @Value("\${application.auth.jwt.secret-key}")
    private lateinit var jwtSecretKey: String

    @Bean
    fun jwsHeader(): JwsHeader {
        return JwsHeader.with(MacAlgorithm.HS256).type("JWT").build()
    }

    @Bean
    fun decoder(): ReactiveJwtDecoder {
        return NimbusReactiveJwtDecoder.withSecretKey(
            SecretKeySpec(
                jwtSecretKey.toByteArray(Charsets.UTF_8),
                MacAlgorithm.HS256.name
            )
        ).build()
    }
}