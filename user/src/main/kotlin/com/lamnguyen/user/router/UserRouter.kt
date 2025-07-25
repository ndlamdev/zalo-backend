/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 7:25 PM-15/07/2025
 *  User: kimin
 **/

package com.lamnguyen.user.router

import com.lamnguyen.user.handlers.UserHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse

@Configuration(proxyBeanMethods = false)
class UserRouter(val userHandler: UserHandler) {

    @Bean("user-router-function")
    fun userRouter(): RouterFunction<ServerResponse> {
        return RouterFunctions
            .route()
            .path("/v1") { v1 ->
                v1.GET("/search", userHandler::search)
                    .POST("/add-friend", userHandler::addFriend)
            }
            .build()
    }

}