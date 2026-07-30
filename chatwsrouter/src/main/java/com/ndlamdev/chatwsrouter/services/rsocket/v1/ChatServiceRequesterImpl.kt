/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 10:43 AM-26/07/2026
 *  User: ndlamdev
 **/

package com.ndlamdev.chatwsrouter.services.rsocket.v1

import com.ndlamdev.chatwsrouter.domain.dto.ConversationDto
import com.ndlamdev.chatwsrouter.domain.request.ConversationInfoRequest
import com.ndlamdev.chatwsrouter.services.rsocket.IChatServiceRequester
import com.ndlamdev.chatwsrouter.services.rsocket.RSocketRequesterManager
import com.ndlamdev.chatwsrouter.utils.enums.RSocketServerName
import org.springframework.messaging.rsocket.retrieveMono
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class ChatServiceRequesterImpl(manager: RSocketRequesterManager) : IChatServiceRequester {
    val chatRequester =
        manager.get(RSocketServerName.ChatServiceRequester) ?: throw Exception("ChatServiceRequester not set")

    override fun getConversationInfo(owner: String, users: List<String>): Mono<ConversationDto> {
        return chatRequester.route("chat.conversation")
            .data(ConversationInfoRequest(owner, users)).retrieveMono<ConversationDto>()
    }

    override fun getConversationInfo(conversationId: String): Mono<ConversationDto> {
        return chatRequester.route("chat.conversation.$conversationId")
            .retrieveMono<ConversationDto>()
    }
}