/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 5:13 PM-28/07/2025
 *  User: kimin
 **/

package com.lamnguyen.chat.repositories

import com.lamnguyen.chat.entities.RoomChat
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.r2dbc.repository.R2dbcRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface IRomChatRepository : R2dbcRepository<RoomChat, String> {
    @Query(
        value = """
        SELECT rc.*
        FROM "zalo-chat".public.room_chats rc
        JOIN "zalo-chat".public.room_chat_members rcm on rc.id = rcm.room_chat_id
        WHERE rcm.phone_number = :phoneNumber
            AND rc.type = 'GROUP'
    """
    )
    fun findAllByPhoneNumberContainAndTypeIsGroup(phoneNumber: String): Flux<RoomChat>
    fun findBySoftId(roomChatId: String): Mono<RoomChat>
    fun findAllBySoftIdStartsWithAndType(phoneNumber: String, type: RoomChat.RoomChatType): Flux<RoomChat>
    fun existsBySoftId(softId: String): Mono<Boolean>
}