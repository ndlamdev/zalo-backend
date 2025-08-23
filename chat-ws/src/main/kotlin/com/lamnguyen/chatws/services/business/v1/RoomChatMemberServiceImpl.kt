/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 12:31 PM-23/08/2025
 *  User: kimin
 **/

package com.lamnguyen.chatws.services.business.v1

import com.lamnguyen.chatws.services.business.IRoomChatMemberService
import com.lamnguyen.chatws.services.redis.IRoomChatMemberCacheManager
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class RoomChatMemberServiceImpl(
    val roomChatMemberCacheManager: IRoomChatMemberCacheManager,
) : IRoomChatMemberService {
    override fun getRoomChatMember(roomChatId: Long): Flux<String> {
        return roomChatMemberCacheManager.getRoomChatMember(roomChatId)
    }
}