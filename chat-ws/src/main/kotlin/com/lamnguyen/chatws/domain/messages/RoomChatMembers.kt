package com.lamnguyen.chatws.domain.messages

data class RoomChatMembers(
    val roomChatId: String,
    val members: List<String>
)