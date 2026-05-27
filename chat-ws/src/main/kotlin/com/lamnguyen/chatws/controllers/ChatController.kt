/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 4:03 AM-05/07/2025
 *  User: kimin
 **/

package com.lamnguyen.chatws.controllers

import com.lamnguyen.chatws.configs.handlers.UserHandshakeHandler
import com.lamnguyen.chatws.domain.messages.ChatMessage
import com.lamnguyen.chatws.domain.requests.Message
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.header.internals.RecordHeaders
import org.springframework.http.HttpHeaders
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.annotation.SendToUser
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller


@Controller
class ChatController(
    private val template: KafkaTemplate<String, ChatMessage>,
) {
    @MessageMapping("/chat")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @SendToUser("/topic/messages")
    fun handleTextMessage(message: Message, principal: UserHandshakeHandler.StompPrincipal): Message {
        return message
    }


    private fun sendMessage(message: Message, principal: UserHandshakeHandler.StompPrincipal) {
        val headers = RecordHeaders()
        headers.add(HttpHeaders.AUTHORIZATION, principal.token.toByteArray())

        val data = ProducerRecord(
            "messages",
            null,
            message.javaClass.name,
            ChatMessage(message).apply {
                senderPhoneNumber = principal.name
            },
            headers
        )
        template.send(data)
    }
}