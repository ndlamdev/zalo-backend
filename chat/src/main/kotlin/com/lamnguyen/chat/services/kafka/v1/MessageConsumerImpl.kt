/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 12:59 PM-26/08/2025
 *  User: kimin
 **/

package com.lamnguyen.chat.services.kafka.v1

import com.lamnguyen.chat.domain.message.ChatMessage
import com.lamnguyen.chat.mappers.IMessageMap
import com.lamnguyen.chat.services.business.IConversationService
import com.lamnguyen.chat.services.business.IMessageService
import com.lamnguyen.chat.services.kafka.IMessageConsumer
import com.lamnguyen.chat.utils.helpers.KeyGenerator
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.stereotype.Service

@Service
class MessageConsumerImpl(
    val messageService: IMessageService,
    val conversationService: IConversationService,
    val messageMapper: IMessageMap,
) : IMessageConsumer {
    override fun saveMessage(consumerRecord: ConsumerRecord<String, ChatMessage>) {
        val chatMessage = consumerRecord.value()

        if (!chatMessage.conversationId.isNullOrBlank()) {
            val message = messageMapper.toEntity(chatMessage)
            messageService.save(message).subscribe()
            return
        }
        conversationService.createConversation(
            chatMessage.senderPhoneNumber!!,
            chatMessage.users.toMutableList()
        ).flatMap {
            val message = messageMapper.toEntity(chatMessage)
            message.conversationId = it.id
            message.id = KeyGenerator.generateUuidV7()
            message.isNewItem = true
            message.senderId = it.admin
            messageService.save(message)
        }.subscribe()
    }
}