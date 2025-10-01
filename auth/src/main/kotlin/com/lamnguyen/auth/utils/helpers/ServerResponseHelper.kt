/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 5:30 PM-10/07/2025
 *  User: kimin
 **/

package com.lamnguyen.auth.utils.helpers

import com.fasterxml.jackson.databind.ObjectMapper
import com.lamnguyen.auth.domain.dto.ApiResponseError
import com.lamnguyen.auth.domain.dto.ApiResponseSuccess
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.function.Consumer

fun <T : Any> ok(data: T?, message: String? = "Response Success!"): Mono<ServerResponse?> {
    return ServerResponse.ok().body(BodyInserters.fromValue(ApiResponseSuccess<Any>().apply {
        code = 200
        this.data = data
        this.message = message ?: "Response Success!"
    }))
}

fun <T : Any> ok(
    data: T?,
    message: String? = "Response Success!",
    headers: Consumer<HttpHeaders>,
): Mono<ServerResponse?> {
    return ServerResponse.ok().headers(headers).body(BodyInserters.fromValue(ApiResponseSuccess<Any>().apply {
        code = 200
        this.data = data
        this.message = message ?: "Response Success!"
    }))
}

fun okTextEventStream(data: Flux<String>): Mono<ServerResponse?> {
    return ServerResponse.ok()
        .contentType(MediaType.TEXT_EVENT_STREAM)
        .body(data, String::class.java)
}

inline fun <reified T : Any> error(
    ex: Throwable,
    code: Int?,
    message: String?,
    detail: T?,
): Mono<ServerResponse?> {
    return ServerResponse.badRequest().body(BodyInserters.fromValue(ApiResponseError<T>().apply {
        this.code = code ?: 400
        error = message ?: ex.message
        this.detail = ObjectMapper().readValue(detail?.toString() ?: "{}", T::class.java)
        trace = ex.stackTrace
    }))
}