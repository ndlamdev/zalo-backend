/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 3:54 PM-08/07/2025
 *  User: kimin
 **/

package com.lamnguyen.auth.security.filters

import com.lamnguyen.auth.model.dto.JWTPayload
import com.lamnguyen.auth.utils.Keyword
import com.lamnguyen.auth.utils.enums.JwtTokenType
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

@Component
class CheckBlacklistTokenFilter(val redisTemplate: ReactiveRedisTemplate<String, Any>) : WebFilter {
    override fun filter(
        exchange: ServerWebExchange,
        chain: WebFilterChain
    ): Mono<Void?> {
        val authentication = SecurityContextHolder.getContext().authentication

        if (authentication !is JwtAuthenticationToken)
            return chain.filter(exchange)

        val payload = authentication.token.getClaim<JWTPayload>(Keyword.PAYLOAD.name)
            ?: return Mono.error(RuntimeException("Missing JWT payload"))

        val issuedAt = authentication.token.issuedAt?.epochSecond
            ?: return Mono.error(RuntimeException("Missing token issuedAt"))

        return Mono.zip(
            redisTemplate.opsForValue()
                .get(Keyword.CHANGE_PASSWORD)
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

                return@flatMap chain.filter(exchange)
            }
    }
}