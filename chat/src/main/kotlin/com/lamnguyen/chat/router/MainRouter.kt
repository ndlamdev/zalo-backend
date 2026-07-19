/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 5:03 PM-28/07/2025
 *  User: kimin
 **/

package com.lamnguyen.chat.router

import com.lamnguyen.chat.domain.requests.CreateConversationRequest
import com.lamnguyen.chat.handlers.ConversationHandler
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
            path = "/v1/conversation",
            method = [RequestMethod.POST],
            beanClass = ConversationHandler::class,
            beanMethod = "createRoomChat",
            operation = Operation(
                operationId = "create-conversation-form",
                security = [SecurityRequirement(name = "bearer-auth")],
                requestBody = RequestBody(
                    required = true,
                    content = [
                        Content(
                            mediaType = "application/json",
                            schema = Schema(implementation = CreateConversationRequest::class)
                        )
                    ]
                )
            )
        ),
        RouterOperation(
            path = "/v1/conversations",
            method = [RequestMethod.GET],
            beanClass = ConversationHandler::class,
            beanMethod = "getAllRoomChat",
            operation = Operation(
                operationId = "get-conversation",
                security = [SecurityRequirement(name = "bearer-auth")],
            )
        )
    )
    @Bean("chat-router-function")
    fun chatRouter(charHandler: ConversationHandler): RouterFunction<ServerResponse> {
        return RouterFunctions
            .route()
            // @formatter:off
            .path("/v1") { v1 -> v1
                .POST("/conversation", charHandler::createConversation)
                .GET("/conversations", charHandler::getAllConversation)
            }
            // @formatter:on
            .build()
    }
}