/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 7:47 PM-26/07/2026
 *  User: ndlamdev
 **/

package com.lamnguyen.chat.mappers

import com.lamnguyen.chat.domain.message.ChatMessage
import com.lamnguyen.chat.entities.Message
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring")
interface IMessageMap {
    @Mapping(target = "messageType", source = "type")
    fun toEntity(message: ChatMessage): Message
}