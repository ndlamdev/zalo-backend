/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 8:52 AM-09/07/2025
 *  User: kimin
 **/

package com.lamnguyen.auth.router

import com.lamnguyen.auth.handlers.AuthenticationHandler
import org.apache.kafka.common.errors.ResourceNotFoundException
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.RouterFunctions.route
import org.springframework.web.reactive.function.server.ServerResponse


@Configuration(proxyBeanMethods = false)
class AuthenticationRouter {
    @Bean
    fun authenticationRoute(authenticationHandler: AuthenticationHandler): RouterFunction<ServerResponse?> {
        return RouterFunctions
            .route()
            .path("/v1") { v1 -> v1.POST("/login", authenticationHandler::login) }
            .build()
    }
}