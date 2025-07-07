/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 4:32 AM-05/07/2025
 *  User: kimin
 **/

package com.lamnguyen.chatws.services.consumers

import com.lamnguyen.chatws.domain.message.ChatMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Component


@Component
class KafkaConsumer(private val messagingTemplate: SimpMessagingTemplate) {

    @KafkaListener(topics = ["chat-message"], groupId = "send-message")
    fun listen(message: ChatMessage) {
        messagingTemplate.convertAndSend("/room/${message.roomChatId}", message)
    }
}