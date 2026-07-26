/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 8:22 PM-15/07/2025
 *  User: kimin
 **/

package com.lamnguyen.chatws.security

import com.lamnguyen.chatws.security.filters.JwtAuthenticationFilter
import com.lamnguyen.chatws.utils.helpers.JwtHelper
import com.lamnguyen.chatws.utils.properties.ApplicationProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val applicationProperty: ApplicationProperty,
) {
    @Bean
    fun securityFilterChain(httpSecurity: HttpSecurity): SecurityFilterChain? {
        return httpSecurity
            .authorizeHttpRequests { exchange ->
                exchange.requestMatchers(*applicationProperty.whitelist.toTypedArray()).permitAll()
                    .anyRequest().authenticated()
            }
            .cors { configurer -> configurer.disable() }
            .csrf { csrf -> csrf.disable() }
            .build()
    }
}