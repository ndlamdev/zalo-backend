package com.lamnguyen.chat.domain.dto

import com.lamnguyen.chat.utils.enums.ConversationType
import com.lamnguyen.chat.utils.enums.MemberRole
import java.time.LocalDateTime

data class ConversationMemberRowDto(
    // conversation c.*
    val id: String,
    val admin: String?,
    val type: ConversationType?,
    val title: String?,
    val avatarUrl: String?,
    val theme: String?,
    val lastMessageId: String?,
    val lastMessageAt: LocalDateTime?,
    val isMuted: Boolean,

    // viewer_metadata
    val viewerIsPinned: Boolean?,

    // member
    val memberId: String,
    val memberUserId: String,
    val memberRole: MemberRole,
    val memberJoinedAt: LocalDateTime?,
    val memberJoinedBy: String?,
    val memberIsMuted: Boolean,
    val memberIsActive: Boolean,

    // member_metadata
    val memberMetadataId: String?,
    val memberLastReadMessageAt: LocalDateTime?,
    val memberIsPinned: Boolean?,
    val memberMetadataIsMuted: Boolean?,
    val memberIsArchived: Boolean?
)