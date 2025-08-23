/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 12:57 PM-23/08/2025
 *  User: kimin
 **/

package com.lamnguyen.chat.services.kafka

import com.lamnguyen.chat.domain.messages.DumpRoomChatMemberMessage
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class RoomChatMemberProducerImpl(val kafkaTemplate: KafkaTemplate<String, Any>) : IRoomChatMemberProducer {
    override fun dumpRoomChatMember(roomChatId: Long, members: List<String>) {
        kafkaTemplate.send("dump-room-chat-member", DumpRoomChatMemberMessage(roomChatId, members))
    }
}