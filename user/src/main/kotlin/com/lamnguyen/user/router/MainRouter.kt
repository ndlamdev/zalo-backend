/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 7:25 PM-15/07/2025
 *  User: kimin
 **/

package com.lamnguyen.user.router

import com.lamnguyen.user.domain.request.InviteAddFriendRequest
import com.lamnguyen.user.handlers.UserHandler
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
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
class MainRouter(val userHandler: UserHandler) {

    @RouterOperations(
        RouterOperation(
            path = "/v1/search",
            method = [RequestMethod.GET],
            beanClass = UserHandler::class,
            beanMethod = "search",
            operation = Operation(
                operationId = "search",
                security = [SecurityRequirement(name = "bearer-auth")],
                parameters = [Parameter(
                    name = "phone_number",
                    `in` = ParameterIn.QUERY,
                    description = "Phone number format: '+phone-number-code/phone-number",
                    example = "+84/08745687912",
                    schema = Schema(implementation = String::class, pattern = "\\+\\d{1,}/\\d{9,}"),
                )]
            )
        ),
        RouterOperation(
            path = "/v1/add-friend",
            method = [RequestMethod.POST],
            beanClass = UserHandler::class,
            beanMethod = "addFriend",
            operation = Operation(
                operationId = "add-friend",
                security = [SecurityRequirement(name = "bearer-auth")],
                requestBody = RequestBody(
                    required = true,
                    content = [
                        Content(
                            mediaType = "application/json",
                            schema = Schema(implementation = InviteAddFriendRequest::class)
                        )
                    ]
                )
            )
        )
    )
    @Bean("user-router-function")
    fun userRouter(): RouterFunction<ServerResponse> {
        return RouterFunctions
            .route()
            // @formatter:off
            .path("/v1") { v1 -> v1
                .GET("/search", userHandler::search)
                .POST("/add-friend", userHandler::addFriend)
            }
            // @formatter:on
            .build()
    }

}