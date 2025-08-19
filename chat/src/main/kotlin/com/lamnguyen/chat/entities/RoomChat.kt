/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 4:56 PM-28/07/2025
 *  User: kimin
 **/

package com.lamnguyen.chat.entities

import org.springframework.data.annotation.Transient
import org.springframework.data.relational.core.mapping.Table

@Table("room_chats")
class RoomChat : BaseEntity() {
    lateinit var title: String
    lateinit var avatar: String
    lateinit var theme: String

    @Transient
    var pin: Boolean = false
}