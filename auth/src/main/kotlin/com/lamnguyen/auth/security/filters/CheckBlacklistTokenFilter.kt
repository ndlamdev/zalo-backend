/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 3:54 PM-08/07/2025
 *  User: kimin
 **/

package com.lamnguyen.auth.security.filters

import com.lamnguyen.auth.exceptions.ApplicationException
import com.lamnguyen.auth.exceptions.ExceptionEnum
import com.lamnguyen.auth.service.redis.v1.AccessTokenCacheManager
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import reactor.kotlin.core.util.function.component1
import reactor.kotlin.core.util.function.component2

class CheckBlacklistTokenFilter(
    val accessTokenManager: AccessTokenCacheManager,
) : WebFilter {
    override fun filter(
        exchange: ServerWebExchange,
        chain: WebFilterChain
    ): Mono<Void?> {

        return ReactiveSecurityContextHolder.getContext().flatMap {
            val authentication = it.authentication
            if (authentication !is JwtAuthenticationToken)
                return@flatMap Mono.empty()

            val issuedAt = authentication.token.issuedAt?.epochSecond
                ?: return@flatMap Mono.error(RuntimeException("Missing token issuedAt"))

            Mono.zip(
                accessTokenManager.hasChangePassword(authentication.name, issuedAt).defaultIfEmpty(false),
                accessTokenManager.existsTokenInBlackList(authentication.token.id).defaultIfEmpty(false),
            )
                .flatMap { (hasChangedPassword, isBlacklisted) ->
                    when {
                        hasChangedPassword -> Mono.error(ApplicationException(ExceptionEnum.WRONG_TOKEN_EXPIRED))
                        isBlacklisted -> Mono.error(ApplicationException(ExceptionEnum.BLACKLIST_TOKEN))
                        else -> Mono.empty<Void>()
                    }
                }
        }.then(chain.filter(exchange))
    }
}