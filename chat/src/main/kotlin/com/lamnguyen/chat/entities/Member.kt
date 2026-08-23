package com.lamnguyen.chat.entities

import com.lamnguyen.chat.domain.dto.UserDto
import com.lamnguyen.chat.utils.annotations.JsonDateTimeFormat
import com.lamnguyen.chat.utils.enums.MemberRole
import org.springframework.data.annotation.Transient
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.time.LocalDateTime

@Document("member")
class Member {
    var id: String? = null

    @Field("phone_number")
    var phoneNumber: String? = null

    var role: MemberRole = MemberRole.USER

    @JsonDateTimeFormat
    @Field("joined_at")
    var joinedAt: LocalDateTime? = null

    @Field("joined_by")
    var joinedBy: String? = null

    @Field("metadata")
    var metadata: ConversationMemberMetadata? = null

    @Transient
    var user: UserDto? = null
}
