/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 7:08 AM-17/07/2025
 *  User: kimin
 **/

package com.lamnguyen.user.security.filters

import com.lamnguyen.user.utils.properties.ApplicationProperty
import org.springframework.core.annotation.Order
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.core.context.SecurityContextImpl
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

@Component
@Order(-1)
class AuthenticationFilter(
    val authProperty: ApplicationProperty.Companion.AuthProperty
) : WebFilter {
    override fun filter(
        exchange: ServerWebExchange, chain: WebFilterChain
    ): Mono<Void?> {
        val phoneNumber = exchange.request.headers[authProperty.userPhoneNumber]?.first()
        val roles = exchange.request.headers[authProperty.userRoles]?.map { it -> SimpleGrantedAuthority(it) }
        if (roles == null || phoneNumber == null || phoneNumber.isEmpty()) {
            return chain.filter(exchange)
        }
        val auth = AnonymousAuthenticationToken(phoneNumber, phoneNumber, roles)
        val context = SecurityContextImpl(auth)
        val holder = ReactiveSecurityContextHolder.withSecurityContext(Mono.just(context))
        return chain.filter(exchange).contextWrite(holder)
    }
}