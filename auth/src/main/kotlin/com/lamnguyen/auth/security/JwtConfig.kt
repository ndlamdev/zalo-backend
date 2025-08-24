package com.lamnguyen.auth.security

import com.lamnguyen.auth.utils.properties.ApplicationProperty
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.KeyType
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.source.ImmutableJWKSet
import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm
import org.springframework.security.oauth2.jwt.*
import java.security.KeyFactory
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
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
    private lateinit var rsaPrivateKey: RSAPrivateKey

    @PostConstruct
    fun loadRsaKeys() {
        val publicKeyBytes = Base64.getDecoder().decode(jwtProperty.publicKey)
        val privateKeyBytes = Base64.getDecoder().decode(jwtProperty.privateKey)

        val kf = KeyFactory.getInstance(KeyType.RSA.value)
        rsaPublicKey = kf.generatePublic(X509EncodedKeySpec(publicKeyBytes)) as RSAPublicKey
        rsaPrivateKey = kf.generatePrivate(PKCS8EncodedKeySpec(privateKeyBytes)) as RSAPrivateKey
    }

    @Bean
    fun jwsHeader(): JwsHeader {
        return JwsHeader.with(SignatureAlgorithm.RS256).type("JWT").build()
    }

    @Bean
    fun reactiveJwtDecoder(): ReactiveJwtDecoder {
        return NimbusReactiveJwtDecoder.withPublicKey(rsaPublicKey).build()
    }

    @Bean
    fun jwtEncoder(): JwtEncoder {
        val rsaKey = RSAKey.Builder(rsaPublicKey)
            .privateKey(rsaPrivateKey)
            .build()
        return NimbusJwtEncoder(ImmutableJWKSet(JWKSet(rsaKey)))
    }
}