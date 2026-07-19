package com.lamnguyen.chat.entities

import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("message_history")
class MessageHistory : BaseEntity() {
    @Column("message_id")
    var messageId: Long = 0

    @Column("editor_id")
    var editorId: Long = 0

    @Column("old_content")
    var oldContent: String? = null

    @Column("new_content")
    var newContent: String? = null
}
