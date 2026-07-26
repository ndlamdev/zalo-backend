/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 2:40 PM-25/07/2026
 *  User: ndlamdev
 **/

package com.ndlamdev.chatwsrouter.services.rsocket

import com.ndlamdev.chatwsrouter.domain.dto.ChatMessage
import reactor.core.publisher.Mono

interface IChatWsServiceRequester {
    fun routeMessage(message: ChatMessage): Mono<Void>
}