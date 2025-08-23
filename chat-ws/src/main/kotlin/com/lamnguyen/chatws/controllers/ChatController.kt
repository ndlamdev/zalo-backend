/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 4:03 AM-05/07/2025
 *  User: kimin
 **/

package com.lamnguyen.chatws.controllers

import com.lamnguyen.chatws.domain.messages.ChatMessage
import com.lamnguyen.chatws.services.business.IRoomChatMemberService
import org.apache.kafka.clients.producer.ProducerRecord
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.web.bind.annotation.RestController


@RestController
class ChatController(
    private val template: KafkaTemplate<String, ChatMessage>,
    val roomChatMemberService: IRoomChatMemberService,
) {
    @MessageMapping("/chat")
    fun handleChatMessage(@Payload message: ChatMessage) {
        roomChatMemberService.getRoomChatMember(message.roomChatId)
            .map { it ->
                val data = ProducerRecord("chat-message", it, message)
                template.send(data)
            }.subscribe()
    }
}