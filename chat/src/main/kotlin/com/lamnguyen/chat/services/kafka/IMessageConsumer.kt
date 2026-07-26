/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 12:58 PM-26/08/2025
 *  User: kimin
 **/

package com.lamnguyen.chat.services.kafka

import com.lamnguyen.chat.domain.message.ChatMessage
import com.lamnguyen.chat.entities.Message
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener

interface IMessageConsumer {
    @KafkaListener(topics = ["messages"], groupId = "chat-service")
    fun saveMessage(consumerRecord: ConsumerRecord<String, ChatMessage>)
}
