package com.ndlamdev.chatwsrouter.domain.dto

import com.ndlamdev.chatwsrouter.utils.annotation.JsonDateTimeFormat
import java.time.LocalDateTime

open class BaseDto {
    var id: String? = null

    @JsonDateTimeFormat
    var createdAt: LocalDateTime? = null

    @JsonDateTimeFormat
    var updatedAt: LocalDateTime? = null

    var createdBy: String? = null

    var updatedBy: String? = null

    var isDeleted: Boolean = false
}
