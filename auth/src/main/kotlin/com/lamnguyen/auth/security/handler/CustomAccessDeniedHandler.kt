package com.lamnguyen.auth.security.handler

import com.fasterxml.jackson.databind.ObjectMapper
import com.lamnguyen.auth.domain.dto.ApiResponseError
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class CustomAccessDeniedHandler : ServerAccessDeniedHandler {
    override fun handle(exchange: ServerWebExchange, denied: AccessDeniedException): Mono<Void> {
        val response = exchange.response
        response.statusCode = HttpStatus.FORBIDDEN
        response.headers.contentType = MediaType.APPLICATION_JSON

        val body = ApiResponseError<Any>().apply {
            code = HttpStatus.FORBIDDEN.value()
            error = HttpStatus.FORBIDDEN.reasonPhrase
            detail = "You are not allowed to access this resource"
            trace = denied.stackTrace
        }

        val buffer = response.bufferFactory().wrap(ObjectMapper().writeValueAsBytes(body))
        return response.writeWith(Mono.just(buffer))
    }
}
