package com.lamnguyen.chat.domain.dto

import com.lamnguyen.chat.entities.Conversation
import com.lamnguyen.chat.entities.Member
import com.lamnguyen.chat.entities.Message

class ConversationDto : Conversation() {
    var pinned: Boolean = false
    var members: MutableList<Member> = mutableListOf()
    var lastMessage: Message? = null
    var pinMessages: MutableList<Message> = mutableListOf()
}
