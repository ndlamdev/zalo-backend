/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 7:09 PM-30/07/2025
 *  User: kimin
 **/

package com.lamnguyen.chat.security

import com.lamnguyen.chat.utils.properties.ApplicationProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.security.KeyFactory
import java.security.interfaces.RSAPublicKey
import java.security.spec.X509EncodedKeySpec
import java.util.*

@Configuration
class KeyConfig(val jwtProperty: ApplicationProperty.Companion.AuthProperty.Companion.JwtProperty) {
    @Bean
    fun createRSAPublicKey(): RSAPublicKey {
        val publicKeyBytes = Base64.getDecoder().decode(jwtProperty.publicKey)

        val kf = KeyFactory.getInstance("RSA")
        return kf.generatePublic(X509EncodedKeySpec(publicKeyBytes)) as RSAPublicKey
    }
}