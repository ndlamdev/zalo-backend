/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 2:07 PM-05/07/2026
 *  User: ndlamdev
 **/

package com.lamnguyen.chat.mappers

import com.lamnguyen.chat.domain.dto.ConversationDto
import com.lamnguyen.chat.domain.requests.CreateConversationRequest
import com.lamnguyen.chat.entities.Conversation
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Mapper(componentModel = "spring")
interface IConversationMapper {
    @Mappings(
        Mapping(target = "members", ignore = true),
    )
    fun toEntity(id: String, request: CreateConversationRequest): Conversation

    fun toDto(conversation: Conversation): ConversationDto
}