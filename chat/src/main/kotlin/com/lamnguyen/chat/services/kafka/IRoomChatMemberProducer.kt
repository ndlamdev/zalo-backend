/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 12:55 PM-23/08/2025
 *  User: kimin
 **/

package com.lamnguyen.chat.services.kafka

interface IRoomChatMemberProducer {
    fun dumpRoomChatMember(roomChatId: Long, members: List<String>)
}