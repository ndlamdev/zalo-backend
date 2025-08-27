package com.lamnguyen.chat.domain.messages

data class RoomChatMembers(
    val roomChatId: String,
    val members: List<String>
)