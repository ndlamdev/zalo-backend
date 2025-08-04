/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 5:04 PM-02/08/2025
 *  User: kimin
 **/

package com.lamnguyen.auth.service.redis.v1

import com.lamnguyen.auth.service.redis.ITokenManager
import com.lamnguyen.auth.utils.redis.ACacheRedis
import org.redisson.api.RedissonReactiveClient
import org.springframework.data.redis.core.ReactiveStringRedisTemplate

abstract class ATokenManager(
    redissonClient: RedissonReactiveClient,
    redisTemple: ReactiveStringRedisTemplate,
) : ACacheRedis<String>(redissonClient, redisTemple), ITokenManager {
    companion object {
        private const val ACCESS_TOKEN = "access_token"
        private const val REFRESH_TOKEN = "refresh_token"
        private const val PREFIX_BLACKLIST = "blacklist"
        const val BLACKLIST_ACCESS_TOKEN = "$PREFIX_BLACKLIST:$ACCESS_TOKEN"
        const val BLACKLIST_REFRESH_TOKEN = "$PREFIX_BLACKLIST:$REFRESH_TOKEN"
    }
}