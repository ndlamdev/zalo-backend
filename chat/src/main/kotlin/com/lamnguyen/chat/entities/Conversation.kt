package com.lamnguyen.chat.entities

import com.lamnguyen.chat.utils.enums.ConversationType
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

@Document("conversation")
open class Conversation : BaseEntity() {
    @Field("soft_id")
    var softId: String? = null

    var admin: String? = null

    var type: ConversationType? = ConversationType.PRIVATE

    var title: String? = null

    @Field("avatar_url")
    var avatarUrl: String? = null

    @Field("theme")
    var theme: String? = null

    @Field("members")
    var members: List<Member> = emptyList()
}
