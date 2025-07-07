/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 4:03 AM-05/07/2025
 *  User: kimin
 **/

package com.lamnguyen.chatws.controllers

import com.lamnguyen.chatws.domain.message.ChatMessage
import org.apache.kafka.clients.producer.ProducerRecord
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.web.bind.annotation.RestController


@RestController
class ChatController(private val template: KafkaTemplate<String, ChatMessage>) {
    @MessageMapping("/chat")
    fun handleChatMessage(@Payload message: ChatMessage) {
        val data = ProducerRecord("chat-message", message.roomChatId, message);
        template.send(data)
    }
}