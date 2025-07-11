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
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
@Order(-2)
class GlobalException(val objectMapper: ObjectMapper) : ErrorWebExceptionHandler {
    override fun handle(
        exchange: ServerWebExchange,
        ex: Throwable
    ): Mono<Void?> {
        exchange.response.statusCode = HttpStatus.BAD_REQUEST

        val response = ApiResponseError<Any>().apply {
            error = ex.message
            trace = ex.stackTrace
        }
        when (ex) {
            is ApplicationException -> {
                response.code = ex.code
                response.detail = ex.detail
            }

            is ResponseStatusException -> ApiResponseError<Any>().apply {
                response.code = HttpStatus.PAYMENT_REQUIRED.value()
                response.error = HttpStatus.PAYMENT_REQUIRED.name
                response.detail = ObjectMapper().readValue(ex.reason, Any::class.java)
            }

            else -> {
                response.code = HttpStatus.BAD_REQUEST.value()
                response.error = HttpStatus.BAD_REQUEST.name
            }
        }
        val data = exchange.response.bufferFactory().wrap(objectMapper.writeValueAsBytes(response))

        return exchange.response.writeWith(Mono.just(data))
    }
}