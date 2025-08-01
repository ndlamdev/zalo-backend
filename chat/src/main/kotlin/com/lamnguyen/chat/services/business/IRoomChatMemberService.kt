/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 5:16 PM-28/07/2025
 *  User: kimin
 **/

package com.lamnguyen.chat.services.business

import com.lamnguyen.chat.entities.RoomChatMember
import reactor.core.publisher.Mono

interface IRoomChatMemberService {
    fun addMember(roomCharId: Long, phoneNumber: String): Mono<RoomChatMember>
}