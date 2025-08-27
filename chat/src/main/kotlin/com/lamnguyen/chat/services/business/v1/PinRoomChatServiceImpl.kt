/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 10:35 AM-18/08/2025
 *  User: kimin
 **/

package com.lamnguyen.chat.services.business.v1

import com.lamnguyen.chat.repositories.IPinRoomChatRepository
import com.lamnguyen.chat.services.business.IPinRoomChatService
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class PinRoomChatServiceImpl(val pinRoomChatRepository: IPinRoomChatRepository) : IPinRoomChatService {
    override fun isPin(roomChatId: String, ownerPhoneNumber: String): Mono<Boolean> {
        return pinRoomChatRepository.existsByRoomChatIdAndOwnerPhoneNumber(roomChatId, ownerPhoneNumber)
    }
}