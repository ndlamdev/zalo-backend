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
import org.springframework.http.MediaType
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
        exchange.response.statusCode = HttpStatus.UNAUTHORIZED
        exchange.response.headers.contentType = MediaType.APPLICATION_JSON
        val apiResponse = ApiResponseError<Any>().apply {
            code = HttpStatus.UNAUTHORIZED.value()
            detail = ex.message
            trace = ex.stackTrace as Any
            error = HttpStatus.UNAUTHORIZED.reasonPhrase
        }

        if (ex.message?.startsWith("Jwt expired") == true) {
            apiResponse.code = 9999
            exchange.response.headers["X-Jwt-Expired"] = "9999"
        }

        val dataBuffer = exchange.response.bufferFactory().wrap(ObjectMapper().writeValueAsBytes(apiResponse))
        return exchange.response
            .writeWith(Mono.just(dataBuffer))
    }
}