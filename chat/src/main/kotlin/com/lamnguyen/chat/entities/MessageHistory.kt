package com.lamnguyen.chat.entities

import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

@Document("message_history")
class MessageHistory : BaseEntity() {
    @Field("message_id")
    var messageId: Long = 0

    @Field("editor_id")
    var editorId: Long = 0

    @Field("old_content")
    var oldContent: String? = null

    @Field("new_content")
    var newContent: String? = null
}
