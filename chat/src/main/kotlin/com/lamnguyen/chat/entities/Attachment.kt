package com.lamnguyen.chat.entities

import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

@Document("attachment")
class Attachment : BaseEntity() {
    @Field("message_id")
    var messageId: String? = null

    var url: String? = null

    var type: Byte? = null

    @Field("file_name")
    var fileName: String? = null

    @Field("file_size")
    var fileSize: Long? = null

    var width: Int? = null

    var height: Int? = null

    var duration: Int? = null
}
