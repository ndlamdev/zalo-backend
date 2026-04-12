/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 3:05 PM-08/07/2025
 *  User: kimin
 **/

package com.lamnguyen.auth.security.filters

import com.lamnguyen.auth.domain.responses.TokenResponse
import com.lamnguyen.auth.utils.enums.Keyword
import com.lamnguyen.auth.utils.helpers.JwtHelper
import com.lamnguyen.auth.utils.properties.ApplicationProperty
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseCookie
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import parsePhoneNumber
import reactor.core.publisher.Mono
import java.util.*

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
        return requireServerWebExchangeMatcher
            .matches(exchange)
            .flatMap {
                ReactiveSecurityContextHolder
                    .getContext()
                    .map {
                        val auth = it.authentication
                        if (auth == null || auth !is UsernamePasswordAuthenticationToken)
                            return@map it

                        val refreshTokenId = UUID.randomUUID().toString()
                        val accessTokenId = UUID.randomUUID().toString()
                        val refreshToken = jwtHelper.createRefreshToken(refreshTokenId, auth.name, accessTokenId)
                        val accessToken = jwtHelper.createAccessToken(accessTokenId, auth, refreshTokenId)

                        val refreshTokenCookie =
                            ResponseCookie.from("REFRESH_TOKEN", refreshToken.tokenValue).apply {
                                maxAge(refreshTokenProperty.expires * 60000)
                                httpOnly(true)
                                secure(true)
                                path("/")
                            }.build()

                        exchange.response.addCookie(refreshTokenCookie)
                        val phoneNumber = parsePhoneNumber(auth.name)
                        val response = TokenResponse(
                            phoneNumber.nationalNumber,
                            phoneNumber.countryCode,
                            accessToken.tokenValue,
                            ""
                        )
                        exchange.attributes["TOKEN_RESPONSE"] = response
                        it
                    }
            }
            .then(chain.filter(exchange))
    }
}