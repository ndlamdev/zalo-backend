/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 5:15 PM-28/07/2025
 *  User: kimin
 **/

package com.lamnguyen.chat.services.business

import com.lamnguyen.chat.entities.RoomChat
import reactor.core.publisher.Mono

interface IRoomChatService {
    fun createRoomChat(title: String): Mono<RoomChat>
    fun removeRoomChat(id: Long): Mono<Void>
}