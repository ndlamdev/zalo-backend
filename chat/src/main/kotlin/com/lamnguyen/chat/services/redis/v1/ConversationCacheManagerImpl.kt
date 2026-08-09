package com.lamnguyen.chat.services.redis.v1

import com.lamnguyen.chat.domain.dto.ConversationDto
import com.lamnguyen.chat.entities.Conversation
import com.lamnguyen.chat.services.redis.IConversationCacheManager
import com.lamnguyen.chat.utils.redis.ACacheRedis
import org.redisson.api.RedissonReactiveClient
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class ConversationCacheManagerImpl(
    redissonClient: RedissonReactiveClient,
    redisTemple: ReactiveRedisTemplate<String, ConversationDto>
) : IConversationCacheManager, ACacheRedis<ConversationDto>(redissonClient, redisTemple) {
    override fun lockToCreate(key: String, action: () -> Mono<Conversation>): Mono<Conversation> {
        return executeMono("lock:$key") { lock ->
            if (lock) action().map { it as ConversationDto }
            else Mono.empty()
        }.map { it as Conversation }
    }
}