package com.ndlamdev.chatwsrouter.domain.request

data class ConversationInfoRequest(var ownerPhone: String, var users: List<String>)
