/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 5:09 PM-08/07/2025
 *  User: kimin
 **/

package com.lamnguyen.auth.handlers

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono


@Component
class GreetingHandler {
    fun hello(request: ServerRequest?): Mono<ServerResponse?> {
        return ServerResponse.ok()
            .body(BodyInserters.fromValue(Greeting("Hello world!")))
    }

    data class Greeting(var content: String)
}