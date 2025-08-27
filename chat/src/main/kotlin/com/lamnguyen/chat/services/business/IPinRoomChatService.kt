/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 10:33 AM-18/08/2025
 *  User: kimin
 **/

package com.lamnguyen.chat.services.business

import reactor.core.publisher.Mono

interface IPinRoomChatService {
    fun isPin(roomChatId: String, ownerPhoneNumber: String): Mono<Boolean>
}