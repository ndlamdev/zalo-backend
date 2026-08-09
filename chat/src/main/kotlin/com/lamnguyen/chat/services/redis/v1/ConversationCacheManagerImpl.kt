package com.lamnguyen.chat.services.redis.v1

import com.lamnguyen.chat.domain.dto.ConversationDto
import com.lamnguyen.chat.entities.Conversation
import com.lamnguyen.chat.services.redis.IConversationCacheManager
import com.lamnguyen.chat.utils.redis.ACacheRedis
import org.redisson.api.RedissonReactiveClient
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.util.concurrent.TimeUnit

@Component
class ConversationCacheManagerImpl(
    redissonClient: RedissonReactiveClient,
    redisTemple: ReactiveRedisTemplate<String, ConversationDto>
) : IConversationCacheManager, ACacheRedis<ConversationDto>(redissonClient, redisTemple) {
    override fun lockToCreate(key: String, createMethod: Mono<Conversation>): Mono<Conversation> {
        val locker = redissonClient.getLock("lock:$key")

        val result = locker.tryLock(10, 5, TimeUnit.SECONDS)
            .filter { it }

        return result
            .flatMap { locker.isLocked }
            .filter { it }
            .flatMap { locker.unlock() }
            .then(createMethod)
    }
}