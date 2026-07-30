package com.ndlamdev.chatwsrouter.domain.dto

import com.ndlamdev.chatwsrouter.utils.annotation.JsonDateTimeFormat
import java.time.LocalDateTime

class ConversationDto : BaseDto() {
    var softId: String? = null

    var admin: String? = null

    var type: ConversationType? = ConversationType.PRIVATE

    var title: String? = null

    var avatarUrl: String? = null

    var theme: String? = null

    var lastMessageId: String? = null

    @JsonDateTimeFormat
    var lastMessageAt: LocalDateTime? = null

    var isMuted: Boolean = false
    var pinned: Boolean = false

    var members: MutableList<MemberDto> = mutableListOf()

    companion object {
        enum class ConversationType {
            PRIVATE, GROUP
        }
    }
}
