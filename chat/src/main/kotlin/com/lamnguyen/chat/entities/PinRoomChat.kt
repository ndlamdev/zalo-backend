/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 10:09 AM-18/08/2025
 *  User: kimin
 **/

package com.lamnguyen.chat.entities

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("pin_room_chats")
class PinRoomChat {
    @Id
    var id: Long? = null
    val phoneNumber: String = ""
    val roomChatId: String = ""
}