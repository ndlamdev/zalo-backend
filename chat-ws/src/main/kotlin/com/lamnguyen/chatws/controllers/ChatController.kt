/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 4:03 AM-05/07/2025
 *  User: kimin
 **/

package com.lamnguyen.chatws.controllers

import com.lamnguyen.chatws.domain.messages.ChatMessage
import org.apache.kafka.clients.producer.ProducerRecord
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import java.security.Principal


@Controller
class ChatController(
    private val template: KafkaTemplate<String, ChatMessage>,
) {
    @MessageMapping("/chat")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    fun handleChatMessage(message: ChatMessage, principal: Principal) {
        val data = ProducerRecord(
            "messages",
            message.javaClass.name,
            message.apply {
                senderPhoneNumber = principal.name
            })
        template.send(data)
    }
}