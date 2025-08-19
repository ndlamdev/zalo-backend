/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 4:58 PM-28/07/2025
 *  User: kimin
 **/

package com.lamnguyen.chat.entities

import org.springframework.data.relational.core.mapping.Table

@Table("room_chat_members")
class RoomChatMember : BaseEntity() {
    lateinit var phoneNumber: String
    var romChatId: Long? = null
    lateinit var username: String
    lateinit var displayName: String
    var role: Role = Role.USER

    enum class Role {
        ADMIN, USER
    }
}