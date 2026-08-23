package com.lamnguyen.chat.domain.dto

class ConversationDtoAndPhoneNumberMember(
        val listDto: MutableList<ConversationDto>,
        val listPhone: MutableSet<String>
    )