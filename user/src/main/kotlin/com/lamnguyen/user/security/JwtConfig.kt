package com.lamnguyen.user.security

import com.lamnguyen.user.utils.properties.ApplicationProperty
import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.jose.jws.MacAlgorithm
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm
import org.springframework.security.oauth2.jwt.JwsHeader
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder
import java.security.KeyFactory
import java.security.interfaces.RSAPublicKey
import java.security.spec.X509EncodedKeySpec
import java.util.*

/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 2:55 PM-08/07/2025
 *  User: kimin
 **/

@Configuration
class JwtConfig(val jwtProperty: ApplicationProperty.Companion.AuthProperty.Companion.JwtProperty) {
    private lateinit var rsaPublicKey: RSAPublicKey

    @PostConstruct
    fun loadRsaKeys() {
        val publicKeyBytes = Base64.getDecoder().decode(jwtProperty.publicKey)

        val kf = KeyFactory.getInstance("RSA")
        rsaPublicKey = kf.generatePublic(X509EncodedKeySpec(publicKeyBytes)) as RSAPublicKey
    }

    @Bean
    fun jwsHeader(): JwsHeader {
        return JwsHeader.with(SignatureAlgorithm.RS256).type("JWT").build()
    }

    @Bean
    fun decoder(): ReactiveJwtDecoder {
        return NimbusReactiveJwtDecoder.withPublicKey(rsaPublicKey).build()
    }
}