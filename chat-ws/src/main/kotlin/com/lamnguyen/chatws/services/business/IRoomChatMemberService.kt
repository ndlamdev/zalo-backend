package com.lamnguyen.chatws.services.business

import reactor.core.publisher.Flux

/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 12:30 PM-23/08/2025
 *  User: kimin
 **/
interface IRoomChatMemberService {
    fun getRoomChatMember(roomChatId: String): Flux<String>
    fun cacheRoomChatMember(roomChatId: String, members: List<String>): Flux<String>
}