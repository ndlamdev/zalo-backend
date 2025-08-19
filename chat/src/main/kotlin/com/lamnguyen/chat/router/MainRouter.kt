/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 5:03 PM-28/07/2025
 *  User: kimin
 **/

package com.lamnguyen.chat.router

import com.lamnguyen.chat.domain.request.CreateRoomChatRequest
import com.lamnguyen.chat.handlers.ChatHandler
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
class MainRouter {
    @RouterOperations(
        RouterOperation(
            path = "/v1/create-room-chat",
            method = [RequestMethod.POST],
            beanClass = ChatHandler::class,
            beanMethod = "createRoomChat",
            operation = Operation(
                operationId = "create-room-chat-form",
                security = [SecurityRequirement(name = "bearer-auth")],
                requestBody = RequestBody(
                    required = true,
                    content = [
                        Content(
                            mediaType = "application/json",
                            schema = Schema(implementation = CreateRoomChatRequest::class)
                        )
                    ]
                )
            )
        ),
        RouterOperation(
            path = "/v1/get-all-room-chat",
            method = [RequestMethod.GET],
            beanClass = ChatHandler::class,
            beanMethod = "getAllRoomChat",
            operation = Operation(
                operationId = "get-room-chat",
                security = [SecurityRequirement(name = "bearer-auth")],
            )
        )
    )
    @Bean("chat-router-function")
    fun chatRouter(charHandler: ChatHandler): RouterFunction<ServerResponse> {
        return RouterFunctions
            .route()
            // @formatter:off
            .path("/v1") { v1 -> v1
                .POST("/create-room-chat", charHandler::createRoomChat)
                .GET("/get-all-room-chat", charHandler::getAllRoomChat)
            }
            // @formatter:on
            .build()
    }
}