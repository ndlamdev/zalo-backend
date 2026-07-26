/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 10:39 AM-26/07/2026
 *  User: ndlamdev
 **/

package com.ndlamdev.chatwsrouter.services.rsocket

import com.ndlamdev.chatwsrouter.domain.dto.ConversationDto
import reactor.core.publisher.Mono

interface IChatServiceRequester {
    fun getConversationInfo(owner: String, users: List<String>): Mono<ConversationDto>

    fun getConversationInfo(conversationId: String): Mono<ConversationDto>
}