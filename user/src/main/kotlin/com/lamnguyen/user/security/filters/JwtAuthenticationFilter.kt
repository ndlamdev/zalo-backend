/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 9:15 AM-29/07/2025
 *  User: kimin
 **/

package com.lamnguyen.user.security.filters

import com.auth0.jwt.JWT
import com.fasterxml.jackson.databind.ObjectMapper
import com.lamnguyen.user.domain.dto.JWTPayload
import com.lamnguyen.user.utils.properties.ApplicationProperty
import org.springframework.http.HttpHeaders
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.core.context.SecurityContextImpl
import org.springframework.security.oauth2.jwt.JwsHeader
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono


class JwtAuthenticationFilter(
    val authProperty: ApplicationProperty.Companion.AuthProperty,
    val jwtProperty: ApplicationProperty.Companion.AuthProperty.Companion.JwtProperty,
    val jwsHeader: JwsHeader,
) : WebFilter {
    override fun filter(
        exchange: ServerWebExchange,
        chain: WebFilterChain
    ): Mono<Void?> {
        val jwt = intJwt(exchange)

        if (jwt == null) {
            return chain.filter(exchange)
        }


        val authorities = exchange
            .request
            .headers[authProperty.userRoles]
            ?.map { it -> SimpleGrantedAuthority(it) }?.toMutableSet()
            ?: mutableSetOf()


        val payload = ObjectMapper()
            .convertValue(
                jwt.claims[jwtProperty.claimKey],
                JWTPayload::class.java
            )

        payload.roles?.forEach { role ->
            authorities.add(SimpleGrantedAuthority(role))
        }


        val authentication = JwtAuthenticationToken(
            jwt,
            authorities
        )

        val context = SecurityContextImpl(authentication)
        val contextHolder = ReactiveSecurityContextHolder
            .withSecurityContext(Mono.just(context))
        return chain.filter(exchange)
            .contextWrite(contextHolder)
    }

    fun intJwt(exchange: ServerWebExchange): Jwt? {
        val tokens = exchange.request.headers[HttpHeaders.AUTHORIZATION]
        if (tokens == null || tokens.isEmpty()) {
            return null
        }

        val token = tokens.first().substring(7)

        val decoded = JWT.decode(token)

        val payload = decoded
            .claims[jwtProperty.claimKey]
            ?.asMap() ?: mapOf()

        return Jwt
            .withTokenValue(token)
            .jti(decoded.id)
            .expiresAt(decoded.expiresAtAsInstant)
            .issuedAt(decoded.issuedAtAsInstant)
            .subject(decoded.subject)
            .issuer(decoded.issuer)
            .headers { map ->
                map.putAll(jwsHeader.headers)
            }.claim(jwtProperty.claimKey, payload)
            .build()
    }
}