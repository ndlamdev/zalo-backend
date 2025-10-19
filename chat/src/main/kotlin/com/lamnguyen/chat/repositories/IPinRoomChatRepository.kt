/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 10:32 AM-18/08/2025
 *  User: kimin
 **/

package com.lamnguyen.chat.repositories

import com.lamnguyen.chat.entities.PinRoomChat
import org.springframework.data.r2dbc.repository.R2dbcRepository
import reactor.core.publisher.Mono

interface IPinRoomChatRepository : R2dbcRepository<PinRoomChat, Long> {
    fun existsByRoomChatIdAndPhoneNumber(roomChatId: String, ownerPhoneNumber: String): Mono<Boolean>
}