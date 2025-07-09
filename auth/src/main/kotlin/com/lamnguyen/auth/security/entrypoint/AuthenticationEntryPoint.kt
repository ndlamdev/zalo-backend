/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 3:43 PM-08/07/2025
 *  User: kimin
 **/

package com.lamnguyen.auth.security.entrypoint

import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.server.ServerAuthenticationEntryPoint
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class AuthenticationEntryPoint : ServerAuthenticationEntryPoint {
    override fun commence(
        exchange: ServerWebExchange?,
        ex: AuthenticationException?
    ): Mono<Void?>? {
        return Mono.create { sink -> sink.error(ex?.cause!!) }
    }
}