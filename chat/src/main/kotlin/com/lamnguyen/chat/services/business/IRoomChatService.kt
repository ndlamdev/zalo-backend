/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 5:15 PM-28/07/2025
 *  User: kimin
 **/

package com.lamnguyen.chat.services.business

import com.lamnguyen.chat.entities.RoomChat
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface IRoomChatService {
    fun createRoomChat(room: RoomChat): Mono<RoomChat>
    fun removeRoomChat(id: String): Mono<Void>
    fun getAllRoomChat(phoneNumber: String): Flux<RoomChat>
    fun existRoomChatById(roomChatId: String): Mono<Boolean>
    fun findBySoftId(roomChatId: String): Mono<RoomChat>
    fun existRoomChatBySoftId(softId: String): Mono<Boolean>
}