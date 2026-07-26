/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 12:09 PM-26/07/2026
 *  User: ndlamdev
 **/

package com.ndlamdev.chatwsrouter.services.business

import com.ndlamdev.chatwsrouter.domain.dto.ConversationDto
import reactor.core.publisher.Mono

interface IConversationService {
    fun getConversationInfo(owner: String, users: List<String>): Mono<ConversationDto>

    fun getConversationInfo(conversationId: String): Mono<ConversationDto>
}