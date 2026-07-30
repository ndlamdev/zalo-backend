package com.ndlamdev.chatwsrouter.domain.dto

import com.ndlamdev.chatwsrouter.utils.annotation.JsonDateTimeFormat
import java.time.LocalDateTime

class ConversationMemberMetadataDto : BaseDto() {
    var conversationId: String? = null

    var memberId: String? = null

    @JsonDateTimeFormat
    var lastReadMessageAt: LocalDateTime? = null

    var pinned: Boolean = false

    var muted: Boolean = false

    var archived: Boolean = false
}
