/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 12:23 PM-23/08/2025
 *  User: kimin
 **/

package com.lamnguyen.chatws.services.redis.v1

import com.lamnguyen.chatws.services.redis.IRSocketMetadataCacheManager
import com.lamnguyen.chatws.utils.properties.RSocketMetadataProperty
import com.lamnguyen.chatws.utils.redis.ACacheRedis
import com.lamnguyen.chatws.utils.redis.ICacheRedis
import org.redisson.api.RedissonReactiveClient
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class RSocketMetadataCacheManagerImpl(
    redissonClient: RedissonReactiveClient,
    redisTemple: ReactiveRedisTemplate<String, RSocketMetadataProperty>,
) : ACacheRedis<RSocketMetadataProperty>(
    redissonClient,
    redisTemple
), IRSocketMetadataCacheManager {
    override fun cache(user: String, metadata: RSocketMetadataProperty): Mono<RSocketMetadataProperty> {
        return super.cacheData(generateKey(user), Mono.just(metadata), null, null)
    }

    override fun clear(user: String) {
        super.clearCache(generateKey(user))
    }

    private fun generateKey(user: String): String {
        return ICacheRedis.hashKeys("RSocketMetadata", "ChatWsService", user)
    }
}