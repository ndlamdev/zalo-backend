/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 12:40 PM-23/08/2025
 *  User: kimin
 **/

package com.lamnguyen.chatws.services.kafka.v1

import com.lamnguyen.chatws.domain.messages.DumpRoomChatMemberMessage
import com.lamnguyen.chatws.services.kafka.IRoomChatMemberKafkaService
import com.lamnguyen.chatws.services.redis.IRoomChatMemberCacheManager
import org.springframework.stereotype.Service

@Service
class RoomChatMemberKafkaServiceImpl(
    val roomChatMemberCacheManager: IRoomChatMemberCacheManager,
) : IRoomChatMemberKafkaService {
    override fun dumpRoomChatMember(data: DumpRoomChatMemberMessage) {
        roomChatMemberCacheManager.cacheRoomChatMember(data.roomChatId, data.members)
    }
}