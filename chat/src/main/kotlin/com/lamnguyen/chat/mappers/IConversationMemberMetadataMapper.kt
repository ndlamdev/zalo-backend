/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 2:38 PM-30/07/2025
 *  User: kimin
 **/

package com.lamnguyen.chat.mappers

import com.lamnguyen.chat.domain.dto.ConversationMemberRowDto
import com.lamnguyen.chat.entities.ConversationMemberMetadata
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Mapper(componentModel = "spring")
interface IConversationMemberMetadataMapper {
    @Mappings(
        Mapping(target = "id", source = "memberMetadataId"),
        Mapping(target = "lastReadMessageAt", source = "memberLastReadMessageAt"),
        Mapping(target = "pinned", source = "memberIsPinned"),
        Mapping(target = "muted", source = "memberMetadataIsMuted"),
        Mapping(target = "archived", source = "memberIsArchived"),
    )
    fun toEntity(row: ConversationMemberRowDto): ConversationMemberMetadata
}