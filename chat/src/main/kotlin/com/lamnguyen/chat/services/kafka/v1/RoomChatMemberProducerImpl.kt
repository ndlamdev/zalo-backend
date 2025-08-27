/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 12:57 PM-23/08/2025
 *  User: kimin
 **/

package com.lamnguyen.chat.services.kafka.v1

import com.lamnguyen.chat.domain.messages.RoomChatMembers
import com.lamnguyen.chat.services.kafka.IRoomChatMemberProducer
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class RoomChatMemberProducerImpl(val kafkaTemplate: KafkaTemplate<String, Any>) : IRoomChatMemberProducer {
    override fun dumpRoomChatMember(roomChatId: String, members: List<String>) {
        kafkaTemplate.send("dump-room-chat-members", RoomChatMembers(roomChatId, members))
    }
}