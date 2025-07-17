/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 8:22 PM-15/07/2025
 *  User: kimin
 **/

package com.lamnguyen.user.security

import com.lamnguyen.user.utils.properties.ApplicationProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain

@Configuration
@EnableReactiveMethodSecurity
class SecurityConfig(
    val applicationProperty: ApplicationProperty,
) {
    @Bean
    fun httpConfig(server: ServerHttpSecurity): SecurityWebFilterChain {
        return server
            .authorizeExchange { exchange ->
                exchange.pathMatchers(*applicationProperty.whitelist.toTypedArray()).permitAll()
            }
            .build()
    }
}