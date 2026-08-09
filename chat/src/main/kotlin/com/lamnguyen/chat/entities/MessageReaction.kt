package com.lamnguyen.chat.entities

import org.springframework.data.mongodb.core.mapping.Field

class MessageReaction {
    @Field("message_id")
    var messageId: String? = null

    @Field("member_id")
    var memberId: String? = null

    var emoji: String? = null
}
