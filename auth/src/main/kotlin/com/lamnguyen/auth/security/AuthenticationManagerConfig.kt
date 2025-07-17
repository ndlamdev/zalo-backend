package com.lamnguyen.auth.security

import com.lamnguyen.auth.service.business.ReactiveUserDetailsServiceImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager
import org.springframework.security.crypto.password.PasswordEncoder

/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 5:58 AM-09/07/2025
 *  User: kimin
 **/
@Configuration
class AuthenticationManagerConfig(val passwordEncoder: PasswordEncoder) {
    @Bean
    fun authenticationManager(
        userDetailsService: ReactiveUserDetailsServiceImpl
    ): ReactiveAuthenticationManager {
        return UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService).apply {
            setPasswordEncoder(passwordEncoder)
        }
    }
}