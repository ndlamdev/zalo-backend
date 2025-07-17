/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 7:35 AM-17/07/2025
 *  User: kimin
 **/

package com.lamnguyen.gateway.filters

import com.fasterxml.jackson.databind.ObjectMapper
import com.lamnguyen.gateway.utils.properties.ApplicationProperty
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Component
class AuthenticationGatewayFilterFactory(
    val authProperty: ApplicationProperty.Companion.AuthProperty,
) : AbstractGatewayFilterFactory<Any>() {

    override fun apply(config: Any?): GatewayFilter? {
        return GatewayFilter { exchange, chain ->
            val token = exchange.request.headers[HttpHeaders.AUTHORIZATION]?.firstOrNull()
            WebClient.create("http://localhost:8000")
                .post()
                .uri("/auth/v1/validate")
                .header(HttpHeaders.AUTHORIZATION, token)
                .exchangeToMono { clientResponse ->
                    if (!clientResponse.statusCode().is2xxSuccessful) {
                        val bufferFactory = exchange.response.bufferFactory()
                        val response = mapOf<String, Any>(
                            "code" to HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "error" to HttpStatus.INTERNAL_SERVER_ERROR.name,
                        )
                        return@exchangeToMono exchange.response.writeWith(
                            Mono.just(
                                bufferFactory.wrap(
                                    ObjectMapper().writeValueAsBytes(
                                        response
                                    )
                                )
                            )
                        )
                    }
                    val phoneNumber = clientResponse.headers().header(authProperty.userPhoneNumber).firstOrNull()
                    val roles = clientResponse.headers().header(authProperty.userRoles)

                    val request = exchange.request
                        .mutate()
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .header(authProperty.userPhoneNumber, phoneNumber)
                        .header(authProperty.userRoles, *roles.toTypedArray())
                        .build()

                    chain.filter(exchange.mutate().request(request).build())
                }.onErrorResume { err ->
                    val bufferFactory = exchange.response.bufferFactory()
                    val response = mapOf<String, Any>(
                        "code" to 400,
                        "error" to "Bad request",
                        "detail" to (err.message ?: ""),
                        "trace" to err.stackTrace
                    )
                    exchange.response.writeWith(Mono.just(bufferFactory.wrap(ObjectMapper().writeValueAsBytes(response))))
                }
        }
    }
}

