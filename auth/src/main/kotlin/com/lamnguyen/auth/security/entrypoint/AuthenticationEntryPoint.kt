/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 3:43 PM-08/07/2025
 *  User: kimin
 **/

package com.lamnguyen.auth.security.entrypoint

import com.fasterxml.jackson.databind.ObjectMapper
import com.lamnguyen.auth.domain.dto.ApiResponseError
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.server.ServerAuthenticationEntryPoint
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class AuthenticationEntryPoint : ServerAuthenticationEntryPoint {
    override fun commence(
        exchange: ServerWebExchange,
        ex: AuthenticationException
    ): Mono<Void?>? {
        val apiResponse = ApiResponseError<Any>().apply {
            code = HttpStatus.FORBIDDEN.value()
            trace = ex.stackTrace as Any
            error = ex.message
        }

        val dataBuffer = exchange.response.bufferFactory().wrap(ObjectMapper().writeValueAsBytes(apiResponse))
        return exchange.response.writeWith(Mono.just(dataBuffer))
    }
}