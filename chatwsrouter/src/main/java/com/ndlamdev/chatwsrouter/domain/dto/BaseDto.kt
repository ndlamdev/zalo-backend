package com.ndlamdev.chatwsrouter.domain.dto

import java.time.LocalDateTime

open class BaseDto {
    var id: String? = null

    var createdAt: LocalDateTime? = null

    var updatedAt: LocalDateTime? = null

    var createdBy: String? = null

    var updatedBy: String? = null

    var isDeleted: Boolean = false
}
