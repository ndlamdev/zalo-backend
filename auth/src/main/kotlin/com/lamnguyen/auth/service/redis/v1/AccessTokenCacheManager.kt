package com.lamnguyen.auth.service.redis.v1

import com.lamnguyen.auth.utils.enums.Keyword
import com.lamnguyen.auth.utils.properties.ApplicationProperty
import com.lamnguyen.auth.utils.redis.ICacheRedis
import org.redisson.api.RedissonReactiveClient
import org.springframework.data.redis.core.ReactiveStringRedisTemplate
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit


@Service
class AccessTokenCacheManager(
    redissonClient: RedissonReactiveClient,
    redisTemple: ReactiveStringRedisTemplate,
    val accessTokenProperty: ApplicationProperty.Companion.AuthProperty.Companion.JwtProperty.Companion.AccessTokenProperty
) : ATokenCacheManager(redissonClient, redisTemple) {
    override fun existsTokenInBlackList(tokenId: String): Mono<Boolean> {
        return super.getData(
            ICacheRedis.hashKeys(BLACKLIST_ACCESS_TOKEN, tokenId),
            false,
            null,
            null
        )
            .map { true }
            .switchIfEmpty(Mono.just(false))
    }

    override fun saveTokenInBlackList(
        tokenId: String
    ): Mono<String> {
        return super.cacheData(
            ICacheRedis.hashKeys(BLACKLIST_ACCESS_TOKEN, tokenId),
            { Mono.just("1") },
            accessTokenProperty.expires,
            ChronoUnit.MINUTES
        )
    }

    fun hasChangePassword(phoneNumber: String, issuedAt: Long): Mono<Boolean> {
        return super.getData(
            ICacheRedis.hashKeys(Keyword.CHANGE_PASSWORD.name, phoneNumber),
            false,
            null,
            null
        )
            .map {
                return@map it != null && it.toLong() >= issuedAt
            }
    }

    fun saveTimeChangePassword(phoneNumber: String): Mono<String> {
        return super.cacheData(
            ICacheRedis.hashKeys(Keyword.CHANGE_PASSWORD.name, phoneNumber),
            { Mono.just(LocalDateTime.now().toInstant(ZoneOffset.UTC).epochSecond.toString()) },
            accessTokenProperty.expires,
            ChronoUnit.MINUTES
        )
    }
}