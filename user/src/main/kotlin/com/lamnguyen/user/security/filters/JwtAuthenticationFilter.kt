/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 9:15 AM-29/07/2025
 *  User: kimin
 **/

package com.lamnguyen.user.security.filters

import com.fasterxml.jackson.databind.ObjectMapper
import com.lamnguyen.user.domain.dto.ApiResponseError
import com.lamnguyen.user.utils.helpers.JwtHelper
import com.lamnguyen.user.utils.properties.ApplicationProperty
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.core.context.SecurityContextImpl
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono


class JwtAuthenticationFilter(
    val authProperty: ApplicationProperty.Companion.AuthProperty,
    val jwtHelper: JwtHelper
) : WebFilter {
    override fun filter(
        exchange: ServerWebExchange,
        chain: WebFilterChain
    ): Mono<Void?> {
        val tokens = exchange.request.headers[HttpHeaders.AUTHORIZATION]
        if (tokens == null || tokens.isEmpty()) {
            return chain.filter(exchange)
        }

        val token = tokens.first().substring(7)

        try {
            val authorities = exchange
                .request
                .headers[authProperty.userRoles]
                ?.map { it -> SimpleGrantedAuthority(it) }
                ?.toMutableSet()
                ?: mutableSetOf()

            val authentication = jwtHelper.initJwtAuthenticationToken(token, authorities)

            val context = SecurityContextImpl(authentication)
            val contextHolder = ReactiveSecurityContextHolder
                .withSecurityContext(Mono.just(context))
            return chain.filter(exchange)
                .contextWrite(contextHolder)
        } catch (e: Exception) {
            val bufferFactory = exchange.response.bufferFactory()
            exchange.response.statusCode = HttpStatus.FORBIDDEN
            exchange.response.headers.contentType = MediaType.APPLICATION_JSON
            val bodyResponse = ApiResponseError<Any>().apply {
                code = HttpStatus.FORBIDDEN.value()
                error = HttpStatus.FORBIDDEN.reasonPhrase
                detail = e.localizedMessage
                trace = e.stackTrace
            }
            val response = bufferFactory.wrap(ObjectMapper().writeValueAsBytes(bodyResponse))
            return exchange.response.writeWith(Mono.just(response))
        }
    }
}