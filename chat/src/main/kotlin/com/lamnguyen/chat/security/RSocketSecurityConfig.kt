/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 9:06 AM-19/07/2026
 *  User: ndlamdev
 **/

package com.lamnguyen.chat.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.rsocket.EnableRSocketSecurity
import org.springframework.security.config.annotation.rsocket.RSocketSecurity
import org.springframework.security.config.annotation.rsocket.RSocketSecurity.AuthorizePayloadsSpec
import org.springframework.security.rsocket.core.PayloadSocketAcceptorInterceptor


@Configuration
@EnableRSocketSecurity
class RSocketSecurityConfig {
    @Bean
    fun rsocketInterceptor(rsocket: RSocketSecurity): PayloadSocketAcceptorInterceptor? {
        rsocket
            .authorizePayload { authorize: AuthorizePayloadsSpec? ->
                authorize!!
                    .setup().permitAll()
                    .anyRequest().permitAll()
            }
        return rsocket.build()
    }
}