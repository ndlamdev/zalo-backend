/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 7:35 AM-17/07/2025
 *  User: kimin
 **/

package com.lamnguyen.gateway.filters

import com.fasterxml.jackson.databind.ObjectMapper
import com.lamnguyen.gateway.dto.ApiResponseError
import com.lamnguyen.gateway.utils.properties.ApplicationProperty
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class AuthenticationGatewayFilterFactory(
    val authProperty: ApplicationProperty.Companion.AuthProperty,
) : AbstractGatewayFilterFactory<Any>() {

    override fun apply(config: Any?): GatewayFilter? {
        return GatewayFilter { exchange, chain ->
            val token = exchange.request.headers[HttpHeaders.AUTHORIZATION]?.firstOrNull()
            if (token.isNullOrEmpty()) return@GatewayFilter chain.filter(exchange)
            WebClient.create("http://localhost:8000")
                .post()
                .uri("/auth/v1/validate")
                .header(HttpHeaders.AUTHORIZATION, token)
                .exchangeToMono { clientResponse ->
                    if (clientResponse.statusCode().is4xxClientError)
                        return@exchangeToMono onResponseErrorFromServer(clientResponse, exchange)

                    if (clientResponse.statusCode().is5xxServerError)
                        return@exchangeToMono onServerException(clientResponse, exchange)

                    if (clientResponse.statusCode().is2xxSuccessful)
                        return@exchangeToMono onResponseSuccess(token, clientResponse, exchange, chain)

                    return@exchangeToMono chain.filter(exchange)
                }.onErrorResume { err ->
                    val bufferFactory = exchange.response.bufferFactory()
                    val response = mapOf<String, Any?>(
                        "code" to HttpStatus.BAD_REQUEST.value(),
                        "error" to HttpStatus.BAD_REQUEST.reasonPhrase,
                        "detail" to err.message,
                        "trace" to err.stackTrace
                    )
                    exchange.response.headers.contentType = MediaType.APPLICATION_JSON
                    exchange.response.statusCode = HttpStatus.BAD_REQUEST
                    exchange.response.writeWith(Mono.just(bufferFactory.wrap(ObjectMapper().writeValueAsBytes(response))))
                }
        }
    }

    private fun onResponseErrorFromServer(clientResponse: ClientResponse, exchange: ServerWebExchange): Mono<Void> {
        val bufferFactory = exchange.response.bufferFactory()
        exchange.response.headers.contentType = MediaType.APPLICATION_JSON
        exchange.response.statusCode = HttpStatus.BAD_REQUEST
        return clientResponse.bodyToMono(Any::class.java)
            .flatMap {
                val response = Mono.just(
                    bufferFactory.wrap(
                        ObjectMapper().writeValueAsBytes(it)
                    )
                )
                exchange.response.statusCode = HttpStatus.BAD_REQUEST
                exchange.response.writeWith(response)
            }
    }

    private fun onServerException(clientResponse: ClientResponse, exchange: ServerWebExchange): Mono<Void> {
        val bufferFactory = exchange.response.bufferFactory()
        exchange.response.headers.contentType = MediaType.APPLICATION_JSON
        exchange.response.statusCode = HttpStatus.INTERNAL_SERVER_ERROR
        return clientResponse.bodyToMono(Exception::class.java)
            .flatMap {
                val bodyResponse = ApiResponseError<Any>().apply {
                    code = HttpStatus.INTERNAL_SERVER_ERROR.value()
                    error = HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase
                    detail = it.localizedMessage
                    trace = it.stackTrace
                }
                val response = bufferFactory.wrap(ObjectMapper().writeValueAsBytes(bodyResponse))
                exchange.response.writeWith(Mono.just(response))
            }
    }


    private fun onResponseSuccess(
        token: String,
        clientResponse: ClientResponse,
        exchange: ServerWebExchange,
        chain: GatewayFilterChain
    ): Mono<Void> {
        val phoneNumber = clientResponse.headers().header(authProperty.userPhoneNumber).firstOrNull()
        val roles = clientResponse.headers().header(authProperty.userRoles)

        val request = exchange.request
            .mutate()
            .header(HttpHeaders.AUTHORIZATION, token)
            .header(authProperty.userPhoneNumber, phoneNumber)
            .header(authProperty.userRoles, *roles.toTypedArray())
            .build()

        return chain.filter(exchange.mutate().request(request).build())
    }
}

