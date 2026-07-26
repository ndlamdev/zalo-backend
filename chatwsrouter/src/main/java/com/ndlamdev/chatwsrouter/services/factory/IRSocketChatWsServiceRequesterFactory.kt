/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 12:49 PM-26/07/2026
 *  User: ndlamdev
 **/

package com.ndlamdev.chatwsrouter.services.factory

import org.springframework.messaging.rsocket.RSocketRequester
import reactor.core.publisher.Mono

interface IRSocketChatWsServiceRequesterFactory {
    fun getRSocketRequester(user: String): Mono<RSocketRequester>
}