/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 4:10 PM-10/07/2025
 *  User: kimin
 **/

package com.lamnguyen.auth.exceptions

import com.fasterxml.jackson.databind.ObjectMapper
import com.lamnguyen.auth.model.dto.ApiResponseError
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class GlobalException(val objectMapper: ObjectMapper) : ErrorWebExceptionHandler {
    override fun handle(
        exchange: ServerWebExchange,
        ex: Throwable
    ): Mono<Void?> {
        exchange.response.statusCode = HttpStatus.BAD_REQUEST

        val response = when (ex) {
            is ApplicationException -> ApiResponseError<Any>().apply {
                code = ex.code
                error = ex.message
                detail = ex.detail
                trace = ex.stackTrace
            }

            else -> ApiResponseError<String>().apply {
                code = HttpStatus.BAD_REQUEST.value()
                error = HttpStatus.BAD_REQUEST.name
                detail = ex.message
                trace = ex.stackTrace
            }
        }

        return exchange.response.writeWith(
            Mono.just(
                exchange.response.bufferFactory().wrap(objectMapper.writeValueAsBytes(response))
            )
        )
    }
}