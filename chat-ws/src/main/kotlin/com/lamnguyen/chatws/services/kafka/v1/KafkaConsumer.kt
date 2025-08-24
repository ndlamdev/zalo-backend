package com.lamnguyen.chatws.services.kafka.v1

import com.fasterxml.jackson.databind.ObjectMapper
import com.lamnguyen.chatws.domain.messages.ChatMessage
import com.lamnguyen.chatws.services.business.IRoomChatMemberService
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Component

/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 4:32 AM-05/07/2025
 *  User: kimin
 **/
@Component
class KafkaConsumer(
    private val messagingTemplate: SimpMessagingTemplate,
    val roomChatMemberService: IRoomChatMemberService,
) {

    @KafkaListener(topics = ["messages"], groupId = "chat-ws-service")
    fun listen(message: ChatMessage) {
        roomChatMemberService.getRoomChatMember(message.roomChatId)
            .doOnNext { it ->
                messagingTemplate.convertAndSendToUser(it, "/user", message)
            }.subscribe()
    }
}