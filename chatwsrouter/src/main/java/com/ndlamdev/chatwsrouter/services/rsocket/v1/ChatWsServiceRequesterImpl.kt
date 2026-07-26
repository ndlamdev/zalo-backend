/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 10:43 AM-26/07/2026
 *  User: ndlamdev
 **/

package com.ndlamdev.chatwsrouter.services.rsocket.v1

import com.ndlamdev.chatwsrouter.domain.dto.ChatMessage
import com.ndlamdev.chatwsrouter.services.business.IConversationService
import com.ndlamdev.chatwsrouter.services.factory.IRSocketChatWsServiceRequesterFactory
import com.ndlamdev.chatwsrouter.services.rsocket.IChatWsServiceRequester
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Component
class ChatWsServiceRequesterImpl(
    private val rSocketChatWsRequesterServiceFactory: IRSocketChatWsServiceRequesterFactory,
    private val conversationService: IConversationService
) : IChatWsServiceRequester {
    override fun routeMessage(message: ChatMessage): Mono<Void> {
        if (!message.conversationId.isNullOrBlank()) return routerMessageByConversationId(message)

        return conversationService.getConversationInfo(message.senderPhoneNumber!!, message.users)
            .flatMapMany { conversation ->
                Flux.fromIterable(conversation.members)
                    .flatMap { member ->
                        rSocketChatWsRequesterServiceFactory.getRSocketRequester(member.id!!)
                    }.flatMap { rSocket -> rSocket.route("chat.receive").data(message).send() }
            }.then(Mono.empty())
    }

    private fun routerMessageByConversationId(message: ChatMessage): Mono<Void> {
        return conversationService.getConversationInfo(message.conversationId!!)
            .flatMapMany { conversation ->
                Flux.fromIterable(conversation.members)
                    .flatMap { member ->
                        rSocketChatWsRequesterServiceFactory.getRSocketRequester(member.id!!)
                    }.flatMap { rSocket -> rSocket.route("chat.receive").data(message).send() }
            }.then(Mono.empty())
    }
}