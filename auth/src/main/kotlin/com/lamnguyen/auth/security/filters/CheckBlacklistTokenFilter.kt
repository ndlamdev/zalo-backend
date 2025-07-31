/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 3:54 PM-08/07/2025
 *  User: kimin
 **/

package com.lamnguyen.auth.security.filters

import com.fasterxml.jackson.databind.ObjectMapper
import com.lamnguyen.auth.domain.dto.JWTPayload
import com.lamnguyen.auth.utils.enums.JwtTokenType
import com.lamnguyen.auth.utils.enums.Keyword
import com.lamnguyen.auth.utils.properties.ApplicationProperty
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

class CheckBlacklistTokenFilter(
    val redisTemplate: ReactiveRedisTemplate<String, Any>,
    val jwtProperty: ApplicationProperty.Companion.AuthProperty.Companion.JwtProperty
) : WebFilter {
    override fun filter(
        exchange: ServerWebExchange,
        chain: WebFilterChain
    ): Mono<Void?> {

        return ReactiveSecurityContextHolder.getContext().flatMap {
            val authentication = it.authentication
            if (authentication !is JwtAuthenticationToken)
                return@flatMap Mono.empty()

            val payload =
                ObjectMapper().convertValue(authentication.token.getClaim(jwtProperty.claimKey), JWTPayload::class.java)
                    ?: return@flatMap Mono.error(RuntimeException("Missing JWT payload"))

            val issuedAt = authentication.token.issuedAt?.epochSecond
                ?: return@flatMap Mono.error(RuntimeException("Missing token issuedAt"))

            Mono.zip(
                redisTemplate.opsForValue()
                    .get(Keyword.CHANGE_PASSWORD.name)
                    .cast(Long::class.java),

                redisTemplate.opsForValue()
                    .get("${JwtTokenType.ACCESS.name}_${payload.phoneNumber}")
                    .cast(Long::class.java)
            )
                .flatMap { tuple ->
                    val changePassword = tuple.t1
                    val blacklistedToken = tuple.t2

                    if (issuedAt <= changePassword) {
                        return@flatMap Mono.error(RuntimeException("Token issued before password change"))
                    }

                    if (blacklistedToken != null) {
                        return@flatMap Mono.error(RuntimeException("Token has been blacklisted"))
                    }

                    return@flatMap Mono.empty<Void>()
                }
        }.then(chain.filter(exchange))
    }
}