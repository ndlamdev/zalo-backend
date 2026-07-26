/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 12:58 PM-26/08/2025
 *  User: kimin
 **/

package com.ndlamdev.chatwsrouter.services.kafka

import com.ndlamdev.chatwsrouter.domain.dto.ChatMessage
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener

interface IMessageConsumer {
    @KafkaListener(topics = ["messages"], groupId = "ChatWsRouterService")
    fun saveMessage(consumerRecord: ConsumerRecord<String, ChatMessage>)
}
