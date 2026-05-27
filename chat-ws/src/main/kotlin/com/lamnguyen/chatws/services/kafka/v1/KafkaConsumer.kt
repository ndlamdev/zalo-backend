package com.lamnguyen.chatws.services.kafka.v1

import com.lamnguyen.chatws.domain.messages.ChatMessage
import com.lamnguyen.chatws.services.business.IRoomChatMemberService
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.http.HttpHeaders
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
class KafkaConsumer(
    private val messagingTemplate: SimpMessagingTemplate,
    val roomChatMemberService: IRoomChatMemberService,
) {

//    @KafkaListener(topics = ["messages"], groupId = "chat-ws-service")
//    fun listen(consumerRecord: ConsumerRecord<String, ChatMessage>) {
//        val message = consumerRecord.value()
//        val token = String(consumerRecord.headers().lastHeader(HttpHeaders.AUTHORIZATION).value())
//        message.roomChatId.run {
//
//        }
//
//        if (message.roomChatId.startsWith("+")) {
//            messagingTemplate.convertAndSendToUser(message.roomChatId, "/queue/messages", message)
//            messagingTemplate.convertAndSendToUser(message.senderPhoneNumber!!, "/queue/messages", message)
//        } else {
//            roomChatMemberService.getRoomChatMember(message.roomChatId)
//                .switchIfEmpty(
//                    chatGrpcService.getMembersInRoomChat(message.roomChatId, token)
//                        .flatMapMany {
//                            roomChatMemberService.cacheRoomChatMember(
//                                message.roomChatId,
//                                it
//                            )
//                        }
//                )
//                .doOnNext { it ->
//                    messagingTemplate.convertAndSendToUser(it, "/queue/messages", message)
//                }.subscribe()
//        }
//    }
}