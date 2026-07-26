/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 12:44 PM-26/07/2026
 *  User: ndlamdev
 **/

package com.ndlamdev.chatwsrouter.services.redis.v1

import com.ndlamdev.chatwsrouter.domain.dto.RSocketMetadata
import com.ndlamdev.chatwsrouter.services.redis.IRSocketMetadataChatWsServiceCacheManager
import com.ndlamdev.chatwsrouter.utils.redis.ACacheRedis
import com.ndlamdev.chatwsrouter.utils.redis.ICacheRedis
import org.redisson.api.RedissonReactiveClient
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class RSocketMetadataChatWsServiceCacheManagerImpl(
    redissonClient: RedissonReactiveClient,
    redisTemple: ReactiveRedisTemplate<String, RSocketMetadata>
) : IRSocketMetadataChatWsServiceCacheManager, ACacheRedis<RSocketMetadata>(
    redissonClient, redisTemple
) {
    override fun getRSocketMetadata(user: String): Mono<RSocketMetadata> {
        return super.getData(generateKey(user), false, null, null)
    }

    private fun generateKey(user: String): String {
        return ICacheRedis.hashKeys("RSocketMetadata", "ChatWsService", user)
    }
}