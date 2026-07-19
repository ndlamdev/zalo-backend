package com.lamnguyen.chat.domain.dto

import com.lamnguyen.chat.entities.Conversation
import com.lamnguyen.chat.entities.Member

class ConversationDto : Conversation() {
    var pinned: Boolean = false
    var members: MutableList<Member> = mutableListOf()
}
