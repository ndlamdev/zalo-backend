package com.lamnguyen.chat.domain.dto

import org.springframework.data.mongodb.core.mapping.Field

class MemberIdResult(
    var id: String,
    @Field("phone_number")
    var phoneNumber: String
) {
}