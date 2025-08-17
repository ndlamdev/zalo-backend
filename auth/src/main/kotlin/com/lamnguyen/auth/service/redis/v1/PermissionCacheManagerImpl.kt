/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 10:20 AM-12/08/2025
 *  User: kimin
 **/

package com.lamnguyen.auth.service.redis.v1

import com.lamnguyen.auth.model.Permission
import com.lamnguyen.auth.service.redis.IPermissionCacheManager
import com.lamnguyen.auth.utils.redis.ACacheRedis
import com.lamnguyen.auth.utils.redis.ICacheRedis
import org.redisson.api.RedissonReactiveClient
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class PermissionCacheManagerImpl(
    redissonClient: RedissonReactiveClient,
    redisTemple: ReactiveRedisTemplate<String, Permission>
) : ACacheRedis<Permission>(redissonClient, redisTemple), IPermissionCacheManager {
    override fun getPermissions(role: String): Flux<Permission> {
        return super.getAllData(
            generateKey(role),
            null,
            null,
            null
        )
    }

    override fun cachePermissions(
        role: String,
        permissions: Flux<Permission>
    ): Flux<Permission> {
        return super.cacheAllData(
            generateKey(role), { permissions },
            null,
            null,
        )
    }

    override fun cleanPermissions(role: String) {
        return super.clearCache(generateKey(role))
    }

    private fun generateKey(role: String) = ICacheRedis.hashKeys("permission", role)
}