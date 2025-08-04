/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 8:22 PM-15/07/2025
 *  User: kimin
 **/

package com.lamnguyen.chat.security

import com.lamnguyen.chat.security.filters.JwtAuthenticationFilter
import com.lamnguyen.chat.utils.helpers.JwtHelper
import com.lamnguyen.chat.utils.properties.ApplicationProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository

@Configuration
@EnableReactiveMethodSecurity
class SecurityConfig(
    val applicationProperty: ApplicationProperty,
    val authProperty: ApplicationProperty.Companion.AuthProperty,
    val jwtHelper: JwtHelper,
) {
    @Bean
    fun securityFilterChain(httpSecurity: ServerHttpSecurity): SecurityWebFilterChain? {
        return httpSecurity
            .addFilterAfter(
                JwtAuthenticationFilter(authProperty, jwtHelper),
                SecurityWebFiltersOrder.AUTHENTICATION
            )
            .authorizeExchange { exchange ->
                exchange.pathMatchers(*applicationProperty.whitelist.toTypedArray()).permitAll()
                    .anyExchange().authenticated()
            }
            .csrf { csrf -> csrf.disable() }
            .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
            .build()
    }
}