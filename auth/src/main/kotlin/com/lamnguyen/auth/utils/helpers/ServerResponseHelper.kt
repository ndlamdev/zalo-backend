/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 5:30 PM-10/07/2025
 *  User: kimin
 **/

package com.lamnguyen.auth.utils.helpers

import com.lamnguyen.auth.model.dto.ApiResponseSuccess
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

fun <T : Any> ok(data: T, message: String? = "Response Success!"): Mono<ServerResponse?> {
    return ServerResponse.ok().body(BodyInserters.fromValue(ApiResponseSuccess<Any>().apply {
        code = 200
        this.data = data
        this.message = message ?: "Response Success!"
    }))
}