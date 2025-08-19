/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 5:19 PM-28/07/2025
 *  User: kimin
 **/

package com.lamnguyen.chat.services.business.v1

import com.lamnguyen.chat.entities.RoomChatMember
import com.lamnguyen.chat.repositories.IRomChatMemberRepository
import com.lamnguyen.chat.services.business.IRoomChatMemberService
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class RoomChatMemberServiceImpl(val roomChatMemberRepository: IRomChatMemberRepository) : IRoomChatMemberService {
    override fun addMember(
        roomCharId: Long,
        phoneNumber: String,
        role: RoomChatMember.Role?,
    ): Mono<RoomChatMember> {
        return roomChatMemberRepository.save(RoomChatMember().apply {
            this.phoneNumber = phoneNumber
            this.romChatId = roomCharId
            this.role = role ?: RoomChatMember.Role.USER
        })
    }
}