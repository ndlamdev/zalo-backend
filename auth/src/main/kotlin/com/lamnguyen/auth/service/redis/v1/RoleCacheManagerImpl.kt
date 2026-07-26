/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 4:05 PM-11/08/2025
 *  User: kimin
 **/

package com.lamnguyen.auth.service.redis.v1

import com.lamnguyen.auth.model.Role
import com.lamnguyen.auth.service.redis.IRoleCacheManager
import com.lamnguyen.auth.utils.redis.ACacheRedis
import com.lamnguyen.auth.utils.redis.ICacheRedis
import org.redisson.api.RedissonReactiveClient
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class RoleCacheManagerImpl(
    redissonClient: RedissonReactiveClient,
    redisTemple: ReactiveRedisTemplate<String, Role>
) : ACacheRedis<Role>(redissonClient, redisTemple), IRoleCacheManager {
    override fun getRoles(phoneNumber: String): Flux<Role> {
        return super.getAllData(
            generateKey(phoneNumber),
            null,
            null,
            null
        )
    }

    override fun cacheRoles(phoneNumber: String, roles: Flux<Role>): Flux<Role> {
        return super.cacheAllData(
            generateKey(phoneNumber), roles,
            null,
            null
        )
    }

    override fun cleanCache(phoneNumber: String): Mono<Void> {
        return super.clearCache(generateKey(phoneNumber)).then()
    }

    private fun generateKey(phoneNumber: String) = ICacheRedis.hashKeys("role", phoneNumber)
}