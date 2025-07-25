/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 5:06 PM-08/07/2025
 *  User: kimin
 **/

package com.lamnguyen.auth.router

import com.lamnguyen.auth.handlers.GreetingHandler
import org.springdoc.core.annotations.RouterOperation
import org.springdoc.core.annotations.RouterOperations
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.reactive.function.server.RequestPredicates.GET
import org.springframework.web.reactive.function.server.RequestPredicates.accept
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse

@Configuration(proxyBeanMethods = false)
class GreetingRouter {
    @Bean
    @RouterOperations(
        RouterOperation(
            path = "/greeting",
            method = [RequestMethod.GET],
            beanClass = GreetingHandler::class,
            beanMethod = "hello"
        )
    )
    fun greetingRoute(greetingHandler: GreetingHandler): RouterFunction<ServerResponse?> {
        return RouterFunctions
            .route(GET("/greeting").and(accept(MediaType.APPLICATION_JSON)), greetingHandler::hello)
    }
}