/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 8:52 AM-09/07/2025
 *  User: kimin
 **/

package com.lamnguyen.auth.router

import com.lamnguyen.auth.domain.requests.LoginRequest
import com.lamnguyen.auth.domain.requests.PhoneNumberRequest
import com.lamnguyen.auth.domain.requests.RegisterRequest
import com.lamnguyen.auth.handlers.AuthenticationHandler
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springdoc.core.annotations.RouterOperation
import org.springdoc.core.annotations.RouterOperations
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse


@Configuration(proxyBeanMethods = false)
class AuthenticationRouter {
    @Bean
    @RouterOperations(
        RouterOperation(
            path = "/v1/login",
            method = [RequestMethod.POST],
            beanClass = AuthenticationHandler::class,
            beanMethod = "login",
            operation = Operation(
                operationId = "login-form",
                requestBody = RequestBody(
                    required = true,
                    content = [
                        Content(
                            mediaType = "application/json",
                            schema = Schema(implementation = LoginRequest::class)
                        )
                    ]
                )
            )
        ),
        RouterOperation(
            path = "/v1/register",
            method = [RequestMethod.POST],
            beanClass = AuthenticationHandler::class,
            beanMethod = "register",
            operation = Operation(
                operationId = "register-form",
                requestBody = RequestBody(
                    required = true,
                    content = [
                        Content(
                            mediaType = "application/json",
                            schema = Schema(implementation = RegisterRequest::class)
                        )
                    ]
                )
            )
        ),
        RouterOperation(
            path = "/v1/validate",
            method = [RequestMethod.POST],
            beanClass = AuthenticationHandler::class,
            beanMethod = "validate",
            operation = Operation(
                operationId = "validate-request",
                security = [SecurityRequirement(name = "bearer-auth")],
            )
        ),
        RouterOperation(
            path = "/v1/check-phone-number",
            method = [RequestMethod.POST],
            beanClass = AuthenticationHandler::class,
            beanMethod = "checkPhoneNumber",
            operation = Operation(
                operationId = "phone-number-form",
                requestBody = RequestBody(
                    required = true,
                    content = [
                        Content(
                            mediaType = "application/json",
                            schema = Schema(implementation = PhoneNumberRequest::class)
                        )
                    ]
                )
            ),
        ),
        RouterOperation(
            path = "/v1/resign",
            method = [RequestMethod.POST],
            beanClass = AuthenticationHandler::class,
            beanMethod = "resign",
        ),
        RouterOperation(
            path = "/v1/logout",
            method = [RequestMethod.POST],
            beanClass = AuthenticationHandler::class,
            beanMethod = "logout",
        ),
    )
    fun authenticationRoute(authenticationHandler: AuthenticationHandler): RouterFunction<ServerResponse?> {
        return RouterFunctions
            .route()
            .path("/v1") { v1 ->
                v1.POST("/login", authenticationHandler::login)
                v1.POST("/register", authenticationHandler::register)
                v1.POST("/validate", authenticationHandler::validate)
                v1.POST("/check-phone-number", authenticationHandler::checkPhoneNumber)
                v1.POST("/resign", authenticationHandler::resign)
                v1.POST("/logout", authenticationHandler::logout)
            }
            .build()
    }
}