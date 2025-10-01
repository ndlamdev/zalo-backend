/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 8:03 PM-17/08/2025
 *  User: kimin
 **/

package com.lamnguyen.chat.entities

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.lamnguyen.chat.utils.enums.ContentMessageType
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
@Table("messages")
class Message : BaseEntity() {
    @Id
    var id: String? = null
    var senderPhoneNumber: String = ""
    var roomChatId: String = ""
    var content: String = ""
    var type: ContentMessageType = ContentMessageType.TEXT
    var urlMedia: String = ""


    fun copy(): Message {
        val that = Message()
        that.id = this.id
        that.senderPhoneNumber = this.senderPhoneNumber
        that.roomChatId = this.roomChatId
        that.content = this.content
        that.type = this.type
        that.urlMedia = this.urlMedia
        this.copy(that)
        return that
    }
}