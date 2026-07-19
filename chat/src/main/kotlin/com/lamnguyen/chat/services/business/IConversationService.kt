/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 5:15 PM-28/07/2025
 *  User: kimin
 **/

package com.lamnguyen.chat.services.business

import com.lamnguyen.chat.domain.dto.ConversationDto
import com.lamnguyen.chat.domain.requests.CreateConversationRequest
import com.lamnguyen.chat.entities.Conversation
import reactor.core.publisher.Mono

interface IConversationService {
    fun createConversation(createConversationRequest: CreateConversationRequest): Mono<Conversation>
    fun removeConversation(id: String): Mono<Void>
    fun getAllConversation(phoneNumber: String): Mono<List<ConversationDto>>
    fun existConversationById(conversationId: String): Mono<Boolean>
    fun findById(conversationId: String): Mono<Conversation>
}