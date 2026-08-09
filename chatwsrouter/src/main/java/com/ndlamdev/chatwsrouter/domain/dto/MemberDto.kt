package com.ndlamdev.chatwsrouter.domain.dto

import com.ndlamdev.chatwsrouter.utils.annotation.JsonDateTimeFormat
import java.time.LocalDateTime

class MemberDto : BaseDto() {
    var conversationId: String? = null

    var phoneNumber: String? = null

    var role: MemberRole = MemberRole.USER

    @JsonDateTimeFormat
    var joinedAt: LocalDateTime? = null

    var joinedBy: String? = null

    var muted: Boolean = false

    var active: Boolean = true

    companion object {
        enum class MemberRole {
            USER, ADMIN
        }
    }
}
