/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 12:13 PM-26/07/2026
 *  User: ndlamdev
 **/

package com.ndlamdev.chatwsrouter.services.redis.v1

import com.ndlamdev.chatwsrouter.domain.dto.ConversationDto
import com.ndlamdev.chatwsrouter.services.redis.IConversationCacheManager
import com.ndlamdev.chatwsrouter.utils.redis.ACacheRedis
import com.ndlamdev.chatwsrouter.utils.redis.ICacheRedis
import org.redisson.api.RedissonReactiveClient
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.time.temporal.ChronoUnit

@Component
class ConversationCacheManagerImpl(
    redissonClient: RedissonReactiveClient,
    redisTemple: ReactiveRedisTemplate<String, ConversationDto>
) : IConversationCacheManager,
    ACacheRedis<ConversationDto>(
        redissonClient, redisTemple
    ) {
    override fun getConversationInfoByConversationId(conversationId: String): Mono<ConversationDto> {
        return super.getData(generateKey(conversationId), true, 60, ChronoUnit.MINUTES)
    }

    override fun getConversationInfoBySoftId(softId: String): Mono<ConversationDto> {
        return super.getData(generateSoftId(softId), true, 60, ChronoUnit.MINUTES)
    }

    override fun cacheConversation(conversation: Mono<ConversationDto>): Mono<ConversationDto> {
        return conversation.flatMap {
            Mono.zip(
                super.cacheData(generateKey(it.id!!), conversation, 60, ChronoUnit.MINUTES),
                super.cacheData(generateSoftId(it.id!!), conversation, 60, ChronoUnit.MINUTES)
            ).then(conversation)
        }
    }

    private fun generateKey(conversationId: String): String {
        return ICacheRedis.hashKeys("conversation", conversationId)
    }

    private fun generateSoftId(softId: String): String {
        return ICacheRedis.hashKeys("conversation", ICacheRedis.joinKeys("soft-id", softId))
    }
}