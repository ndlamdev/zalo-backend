package com.lamnguyen.chat.entities

import com.lamnguyen.chat.utils.annotations.JsonDateTimeFormat
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.time.LocalDateTime

@Document("conversation_member_metadata")
class ConversationMemberMetadata {
    @JsonDateTimeFormat
    @Field("deleted_conversation_at")
    var deletedConversationAt: LocalDateTime? = LocalDateTime.now()

    @JsonDateTimeFormat
    @Field("last_read_message_at")
    var lastReadMessageAt: LocalDateTime? = LocalDateTime.now()

    @Field("is_pinned")
    var pinned: Boolean = false

    @Field("is_muted")
    var muted: Boolean = false

    @Field("is_archived")
    var archived: Boolean = false
}
