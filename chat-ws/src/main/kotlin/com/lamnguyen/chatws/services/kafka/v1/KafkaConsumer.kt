package com.lamnguyen.chatws.services.kafka.v1

import com.lamnguyen.chatws.domain.messages.ChatMessage
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
class KafkaConsumer(private val messagingTemplate: SimpMessagingTemplate) {

    @KafkaListener(topics = ["chat-message"], groupId = "send-message")
    fun listen(message: ChatMessage) {
        messagingTemplate.convertAndSend("/room/${message.roomChatId}", message)
    }
}