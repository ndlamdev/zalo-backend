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
import com.lamnguyen.chat.services.business.IPinRoomChatService
import com.lamnguyen.chat.services.business.IRoomChatService
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class RoomChatServiceImpl(
    val roomChatRepository: IRomChatRepository,
    val pinRoomChatService: IPinRoomChatService,
) :
    IRoomChatService {
    override fun createRoomChat(room: RoomChat): Mono<RoomChat> {
        return roomChatRepository.save(room)
    }

    override fun removeRoomChat(id: String): Mono<Void> {
        return roomChatRepository.deleteById(id)
    }

    override fun getAllRoomChat(phoneNumber: String): Flux<RoomChat> {
        return roomChatRepository.findAllByPhoneNumberContains(
            phoneNumber
        ).flatMap { roomChat ->
            pinRoomChatService.isPin(roomChat.id!!, phoneNumber)
                .map { exists -> roomChat.apply { pin = exists } }
        }
    }

    override fun existRoomChatById(roomChatId: String): Mono<Boolean> {
        return roomChatRepository.existsById(roomChatId)
    }

    override fun findById(roomChatId: String): Mono<RoomChat> {
        return roomChatRepository.findById(roomChatId)
    }
}