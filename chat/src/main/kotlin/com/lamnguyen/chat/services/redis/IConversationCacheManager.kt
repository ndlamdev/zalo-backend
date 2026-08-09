package com.lamnguyen.chat.services.redis

import com.lamnguyen.chat.domain.dto.ConversationDto
import com.lamnguyen.chat.entities.Conversation
import com.lamnguyen.chat.utils.redis.ICacheRedis
import reactor.core.publisher.Mono

interface IConversationCacheManager : ICacheRedis<ConversationDto> {
    fun lockToCreate(key: String, createMethod: Mono<Conversation>): Mono<Conversation>
}