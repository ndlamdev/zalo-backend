/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 10:01 AM-19/07/2026
 *  User: ndlamdev
 **/

package com.lamnguyen.chat.controller

import com.lamnguyen.chat.domain.dto.ConversationDto
import com.lamnguyen.chat.domain.requests.ConversationInfoRequest
import com.lamnguyen.chat.services.business.IConversationService
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.stereotype.Controller
import reactor.core.publisher.Mono

@Controller
class RSocketController(val conversationService: IConversationService) {
    @MessageMapping("chat.conversation")
    fun getConversation(request: ConversationInfoRequest): Mono<ConversationDto> {
        return conversationService.getConversationInfo(request.ownerPhone, request.users)
            .switchIfEmpty(
                conversationService.createConversation(request.ownerPhone, request.users, "ChatWsRouterService")
                    .flatMap { conversation ->
                        conversationService.getConversationBySoftId(conversation.softId!!)
                    })
    }

    @MessageMapping("chat.conversation.{conversationId}")
    fun getConversation(@DestinationVariable conversationId: String): Mono<ConversationDto> {
        return conversationService.getConversationInfo(conversationId)
    }
}