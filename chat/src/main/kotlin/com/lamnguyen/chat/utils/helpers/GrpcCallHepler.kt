/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 2:14 PM-01/08/2025
 *  User: kimin
 **/

package com.lamnguyen.chat.utils.helpers

import com.lamnguyen.chat.configs.grpc.credentials.BearerTokenCallCredentials
import io.grpc.stub.AbstractStub
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import reactor.core.publisher.Mono

fun <S : AbstractStub<S>> grpcCall(service: AbstractStub<S>): Mono<S> {
    return ReactiveSecurityContextHolder.getContext()
        .mapNotNull {
            val auth = it.authentication
            if (auth !is JwtAuthenticationToken)
                return@mapNotNull null
            service.withCallCredentials(BearerTokenCallCredentials(auth.token.tokenValue))
        }
}