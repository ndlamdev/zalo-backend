/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 12:20 PM-23/08/2025
 *  User: kimin
 **/

package com.lamnguyen.chatws.services.redis

import reactor.core.publisher.Flux

interface IRoomChatMemberCacheManager {
    fun getRoomChatMember(roomChatId: String): Flux<String>
    fun cacheRoomChatMember(roomChatId: String, members: List<String>): Flux<String>
}