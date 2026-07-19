package com.lamnguyen.chat.entities

import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("attachment")
class Attachment : BaseEntity() {
    @Column("message_id")
    var messageId: String? = null

    var url: String? = null

    var type: Byte? = null

    @Column("file_name")
    var fileName: String? = null

    @Column("file_size")
    var fileSize: Long? = null

    var width: Int? = null

    var height: Int? = null

    var duration: Int? = null
}
