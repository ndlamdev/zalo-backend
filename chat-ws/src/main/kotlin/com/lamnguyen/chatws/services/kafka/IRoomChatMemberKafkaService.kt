/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 5:03 AM-15/07/2025
 *  User: kimin
 **/

package com.lamnguyen.chatws.services.kafka

import com.lamnguyen.chatws.domain.messages.DumpRoomChatMemberMessage
import org.springframework.kafka.annotation.KafkaListener

interface IRoomChatMemberKafkaService {
    @KafkaListener(groupId = "chat-ws-service", topics = ["dump-room-chat-member"])
    fun dumpRoomChatMember(data: DumpRoomChatMemberMessage)
}