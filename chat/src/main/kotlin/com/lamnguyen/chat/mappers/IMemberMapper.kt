/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 2:38 PM-30/07/2025
 *  User: kimin
 **/

package com.lamnguyen.chat.mappers

import com.lamnguyen.chat.domain.dto.ConversationMemberRowDto
import com.lamnguyen.chat.entities.Member
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Mapper(componentModel = "spring")
interface IMemberMapper {
    @Mappings(
        Mapping(target = "id", source = "memberId"),
        Mapping(target = "userId", source = "memberUserId"),
        Mapping(target = "active", source = "memberIsActive"),
        Mapping(target = "muted", source = "memberIsMuted"),
        Mapping(target = "role", source = "memberRole"),
        Mapping(target = "joinedAt", source = "memberJoinedAt"),
        Mapping(target = "joinedBy", source = "memberJoinedBy"),
    )
    fun toEntity(row: ConversationMemberRowDto): Member
}