package com.lamnguyen.chatws.domain.messages

data class DumpRoomChatMemberMessage(
    val roomChatId: Long,
    val members: List<String>
)