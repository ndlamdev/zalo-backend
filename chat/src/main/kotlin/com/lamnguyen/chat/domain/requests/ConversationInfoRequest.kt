package com.lamnguyen.chat.domain.requests

data class ConversationInfoRequest(var ownerPhone: String, var users: List<String>)
