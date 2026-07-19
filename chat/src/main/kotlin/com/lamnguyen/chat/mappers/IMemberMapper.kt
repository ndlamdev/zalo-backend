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
        Mapping(target = "id", "memberId"),
        Mapping(target = "userId", "memberUserId"),
        Mapping(target = "active", "memberIsActive"),
        Mapping(target = "muted", "memberIsMuted"),
        Mapping(target = "role", "memberRole"),
        Mapping(target = "joinedAt", "memberJoinedAt"),
        Mapping(target = "joinedBy", "memberJoinedBy"),
    )
    fun toEntity(row: ConversationMemberRowDto): Member

    /**
     *  @Column("conversation_id")
     *     var conversationId: String? = null
     *
     *     @Column("user_id")
     *     var userId: String? = null
     *
     *     var role: MemberRole = MemberRole.USER
     *
     *     @Column("joined_at")
     *     var joinedAt: LocalDateTime? = null
     *
     *     @Column("joined_by")
     *     var joinedBy: String? = null
     *
     *     @Column("is_muted")
     *     var isMuted: Boolean = false
     *
     *     @Column("is_active")
     *     var isActive: Boolean = true
     *
     *     @Transient
     *     var metadata: ConversationMemberMetadata? = null
     */
}