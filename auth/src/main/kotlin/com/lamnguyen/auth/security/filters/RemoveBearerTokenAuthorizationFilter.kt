/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 8:37 AM-07/08/2025
 *  User: kimin
 **/

package com.lamnguyen.auth.security.filters

import com.lamnguyen.auth.utils.properties.ApplicationProperty
import org.springframework.http.HttpHeaders
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class RemoveBearerTokenAuthorizationFilter(applicationProperty: ApplicationProperty) : WebFilter {
    val list = applicationProperty.whitelist.map {
        PathPatternParserServerWebExchangeMatcher(it)
    }

    override fun filter(
        exchange: ServerWebExchange,
        chain: WebFilterChain
    ): Mono<Void?> {
        return Flux.fromIterable(list)
            .flatMap { it.matches(exchange) }
            .filter { it.isMatch }
            .collectList()
            .flatMap {
                if (it.isNullOrEmpty()) return@flatMap chain.filter(exchange)
                val request = exchange.request.mutate().header(HttpHeaders.AUTHORIZATION, null).build()
                return@flatMap chain.filter(exchange.mutate().request(request).build())
            }
    }
}