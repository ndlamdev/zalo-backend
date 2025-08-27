/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 2:14 PM-01/08/2025
 *  User: kimin
 **/

package com.lamnguyen.chatws.utils.helpers

import com.lamnguyen.chatws.utils.credentials.BearerTokenCallCredentials
import io.grpc.stub.AbstractStub
import reactor.core.publisher.Mono

fun <S : AbstractStub<S>> grpcCall(service: AbstractStub<S>, token: String?): Mono<S> {
    return Mono.just(service.withCallCredentials(BearerTokenCallCredentials(token)))
}