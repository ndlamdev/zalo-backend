package com.lamnguyen.chat.domain.messages

data class DumpRoomChatMemberMessage(
    val roomChatId: Long,
    val members: List<String>
)