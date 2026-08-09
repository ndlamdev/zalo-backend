package com.lamnguyen.chat.entities

import org.springframework.data.mongodb.core.mapping.Field
import java.time.LocalDateTime

class MessageStatus {
    @Field("member_id")
    var memberId: String? = null

    var status: Status = Status.RECEIVED

    @Field("status_at")
    var statusAt: LocalDateTime? = null

    enum class Status {
        PENDING, RECEIVED, READ, DELETED, UNKNOWN, SEND
    }
}
