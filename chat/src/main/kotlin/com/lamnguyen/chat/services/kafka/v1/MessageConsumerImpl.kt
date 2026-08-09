/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 12:59 PM-26/08/2025
 *  User: kimin
 **/

package com.lamnguyen.chat.services.kafka.v1

import com.lamnguyen.chat.domain.dto.MemberIdResult
import com.lamnguyen.chat.domain.message.ChatMessage
import com.lamnguyen.chat.entities.Message
import com.lamnguyen.chat.entities.MessageStatus
import com.lamnguyen.chat.mappers.IMessageMapper
import com.lamnguyen.chat.services.business.IConversationService
import com.lamnguyen.chat.services.business.IMemberService
import com.lamnguyen.chat.services.business.IMessageService
import com.lamnguyen.chat.services.kafka.IMessageConsumer
import com.lamnguyen.chat.utils.helpers.KeyGenerator
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class MessageConsumerImpl(
    val messageService: IMessageService,
    val conversationService: IConversationService,
    val messageMapper: IMessageMapper,
    var memberService: IMemberService,
) : IMessageConsumer {
    override fun saveMessage(consumerRecord: ConsumerRecord<String, ChatMessage>) {
        val chatMessage = consumerRecord.value()

        if (!chatMessage.conversationId.isNullOrBlank()) {
            memberService.findAllMemberId(chatMessage.conversationId!!)
                .collectList()
                .flatMap { memberIds ->
                    val message = this.createMessage(chatMessage, memberIds)
                    message.conversationId = chatMessage.conversationId!!
                    messageService.save(message)
                }.subscribe()
            return
        }

        conversationService.createConversation(
            chatMessage.senderPhoneNumber!!,
            chatMessage.users.toMutableList(),
            "ChatService"
        ).flatMap { conversation ->
            val message =
                this.createMessage(chatMessage, conversation.members.map { MemberIdResult(it.id!!, it.phoneNumber!!) })
            message.conversationId = conversation.id
            messageService.save(message)
        }.subscribe()
    }

    private fun createMessage(chatMessage: ChatMessage, members: List<MemberIdResult>): Message {
        val senderId = members.first { it.phoneNumber == chatMessage.senderPhoneNumber }.id
        val time = LocalDateTime.now()
        val message = messageMapper.toEntity(chatMessage).apply {
            this.senderId = senderId
            this.id = KeyGenerator.generateUuidV7()
            this.isNewItem = true
            statuses = members.map { member ->
                MessageStatus().apply {
                    this.memberId = member.id
                    status =
                        if (member.id == senderId) MessageStatus.Status.SEND else MessageStatus.Status.RECEIVED
                    statusAt = time
                }
            }.toSet()
        }
        return message
    }
}