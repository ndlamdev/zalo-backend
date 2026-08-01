/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 4:03 AM-05/07/2025
 *  User: kimin
 **/

package com.lamnguyen.chatws.controllers

import com.lamnguyen.chatws.domain.messages.ChatMessage
import com.lamnguyen.chatws.domain.messages.ReceiveMessage
import com.lamnguyen.chatws.domain.requests.Message
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.header.internals.RecordHeaders
import org.springframework.http.HttpHeaders
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Controller


@Controller
class ChatController(
    private val template: KafkaTemplate<String, ChatMessage>,
    private val messagingTemplate: SimpMessagingTemplate,
) {
    @MessageMapping("/chat")
    fun handleTextMessage(message: Message, token: JwtAuthenticationToken): Message {
        return sendMessage(message, token)
    }

    private fun sendMessage(message: Message, token: JwtAuthenticationToken): Message {
        val headers = RecordHeaders()
        val name = token.name

        headers.add(HttpHeaders.AUTHORIZATION, token.token.tokenValue.toByteArray())
        headers.add("owner", name.toByteArray())

        val data = ProducerRecord(
            "messages",
            null,
            name,
            ChatMessage(message).apply {
                senderPhoneNumber = name
            },
            headers
        )

        template.send(data)

        return message
    }

    @MessageMapping("chat.receive")
    fun receiveMessage(message: ReceiveMessage) {
        messagingTemplate.convertAndSendToUser(message.user, "/queue/chat", message)
    }
}