/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 5:17 PM-28/07/2025
 *  User: kimin
 **/

package com.lamnguyen.chat.services.business.v1

import com.lamnguyen.chat.entities.RoomChat
import com.lamnguyen.chat.repositories.IRomChatRepository
import com.lamnguyen.chat.services.business.IRoomChatService
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class RoomChatServiceImpl(val roomChatRepository: IRomChatRepository) : IRoomChatService {
    override fun createRoomChat(title: String): Mono<RoomChat> {
        return roomChatRepository.save(RoomChat().apply { this.title = title })
    }

    override fun removeRoomChat(id: Long): Mono<Void> {
       return roomChatRepository.deleteById(id)
    }
}