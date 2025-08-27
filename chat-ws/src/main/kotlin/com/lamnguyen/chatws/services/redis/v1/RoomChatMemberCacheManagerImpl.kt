/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 12:23 PM-23/08/2025
 *  User: kimin
 **/

package com.lamnguyen.chatws.services.redis.v1

import com.lamnguyen.chatws.services.redis.IRoomChatMemberCacheManager
import com.lamnguyen.chatws.utils.redis.ACacheRedis
import com.lamnguyen.chatws.utils.redis.ICacheRedis
import org.redisson.api.RedissonReactiveClient
import org.springframework.data.redis.core.ReactiveStringRedisTemplate
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.time.temporal.ChronoUnit

@Service
class RoomChatMemberCacheManagerImpl(
    redissonClient: RedissonReactiveClient,
    redisTemple: ReactiveStringRedisTemplate,
) : ACacheRedis<String>(
    redissonClient,
    redisTemple
), IRoomChatMemberCacheManager {
    override fun getRoomChatMember(roomChatId: String): Flux<String> {
        return super.getAllData(generateKey(roomChatId), null, null, null)
    }

    private fun generateKey(roomChatId: String): String {
        return ICacheRedis.hashKeys("room-chat-member", roomChatId.toString())
    }

    override fun cacheRoomChatMember(roomChatId: String, members: List<String>): Flux<String> {
        super.clearCache(generateKey(roomChatId))
        return super.cacheAllData(
            generateKey(roomChatId),
            { Flux.fromIterable(members) },
            99999999999,
            ChronoUnit.YEARS
        )
    }
}