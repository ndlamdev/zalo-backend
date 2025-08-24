/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 8:03 PM-17/08/2025
 *  User: kimin
 **/

package com.lamnguyen.chat.entities

import com.lamnguyen.chat.utils.enums.ContentMessageType
import org.springframework.data.relational.core.mapping.Table

@Table("messages")
class Message : BaseEntity() {
    var senderPhoneNumber: String = ""
    var roomChatId: Long = 0
    var content: String = ""
    var type: ContentMessageType = ContentMessageType.TEXT
    var urlMedia: String = ""
}