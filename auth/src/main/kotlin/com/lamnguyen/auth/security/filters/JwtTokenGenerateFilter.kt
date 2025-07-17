/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 3:05 PM-08/07/2025
 *  User: kimin
 **/

package com.lamnguyen.auth.security.filters

import com.lamnguyen.auth.utils.helpers.JwtHelper
import com.lamnguyen.auth.utils.properties.ApplicationProperty
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseCookie
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.web.server.context.SecurityContextServerWebExchange
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

class JwtTokenGenerateFilter(
    val jwtHelper: JwtHelper,
    val refreshTokenProperty: ApplicationProperty.Companion.AuthProperty.Companion.JwtProperty.Companion.RefreshTokenProperty
) : WebFilter {
    var requireServerWebExchangeMatcher: ServerWebExchangeMatcher =
        PathPatternParserServerWebExchangeMatcher("/v1/login", HttpMethod.POST)

    override fun filter(
        exchange: ServerWebExchange,
        chain: WebFilterChain
    ): Mono<Void?> {
        if (exchange !is SecurityContextServerWebExchange) return chain.filter(exchange)
        return requireServerWebExchangeMatcher
            .matches(exchange)
            .flatMap {
                ReactiveSecurityContextHolder
                    .getContext()
                    .flatMap { it ->
                        val auth = it.authentication
                        if (auth == null || auth !is UsernamePasswordAuthenticationToken)
                            return@flatMap chain.filter(exchange)

                        val refreshToken = jwtHelper.createRefreshToken(auth)
                        val accessToken = jwtHelper.createAccessToken(auth, refreshToken.id)

                        val refreshTokenCookie =
                            ResponseCookie.from("REFRESH-TOKEN", refreshToken.tokenValue).apply {
                                maxAge(refreshTokenProperty.expires * 60000)
                                httpOnly(true)
                                secure(true)
                            }.build()

                        exchange.response.addCookie(refreshTokenCookie)
                        exchange.response.headers.add(HttpHeaders.AUTHORIZATION, "Bearer ${accessToken.tokenValue}")

                        chain.filter(exchange)
                    }
            }
    }
}