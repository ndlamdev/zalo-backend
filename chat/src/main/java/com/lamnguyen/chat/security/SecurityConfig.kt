/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 6:02 PM-28/07/2025
 *  User: kimin
 **/

package com.lamnguyen.chat.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain

@Configuration
class SecurityConfig {
    @Bean
    fun securityChain(security: ServerHttpSecurity): SecurityWebFilterChain {
        return security
            .authorizeExchange { exchange ->
                exchange.anyExchange().permitAll()
            }
            .build()
    }
}