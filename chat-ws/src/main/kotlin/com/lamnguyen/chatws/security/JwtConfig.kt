package com.lamnguyen.chatws.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import java.security.interfaces.RSAPublicKey

/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 2:55 PM-08/07/2025
 *  User: kimin
 **/

@Configuration
class JwtConfig {
    @Bean
    fun decoder(rsaPublicKey: RSAPublicKey): JwtDecoder {
        return NimbusJwtDecoder
            .withPublicKey(rsaPublicKey)
            .build()
    }
}