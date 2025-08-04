package com.lamnguyen.auth.service.redis.v1

import com.lamnguyen.auth.utils.properties.ApplicationProperty
import com.lamnguyen.auth.utils.redis.ICacheRedis
import org.redisson.api.RedissonReactiveClient
import org.springframework.data.redis.core.ReactiveStringRedisTemplate
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.time.temporal.ChronoUnit

@Service
class RefreshTokenManager(
    redissonClient: RedissonReactiveClient,
    redisTemple: ReactiveStringRedisTemplate,
    val refreshTokenProperty: ApplicationProperty.Companion.AuthProperty.Companion.JwtProperty.Companion.RefreshTokenProperty
) : ATokenManager(redissonClient, redisTemple) {
    override fun existsTokenInBlackList(tokenId: String): Mono<Boolean> {
        return super.getData(
            ICacheRedis.hashKeys(BLACKLIST_REFRESH_TOKEN, tokenId),
            null,
            null
        )
            .map { true }
            .switchIfEmpty(Mono.just(false))
    }

    override fun saveTokenInBlackList(
        tokenId: String,
    ): Mono<String> {
        return super.cacheData(
            ICacheRedis.hashKeys(BLACKLIST_REFRESH_TOKEN, tokenId),
            { Mono.just("1") },
            refreshTokenProperty.expires,
            ChronoUnit.MINUTES
        )
    }
}