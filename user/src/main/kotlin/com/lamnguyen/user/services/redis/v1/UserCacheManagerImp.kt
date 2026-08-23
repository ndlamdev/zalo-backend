package com.lamnguyen.user.services.redis.v1

import com.lamnguyen.user.models.User
import com.lamnguyen.user.services.redis.IUserCacheManager
import com.lamnguyen.user.utils.redis.ACacheRedis
import com.lamnguyen.user.utils.redis.CacheResult
import org.redisson.api.RedissonReactiveClient
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.temporal.ChronoUnit
import java.util.function.Function
import java.util.stream.Collectors

@Component
class UserCacheManagerImp(redissonClient: RedissonReactiveClient, redisTemple: ReactiveRedisTemplate<String, User>) :
    IUserCacheManager,
    ACacheRedis<User>(
        redissonClient,
        redisTemple
    ) {
    override fun get(phone: String): Mono<User> {
        return getData(phone, true)
    }

    override fun cache(key: String, user: Mono<User>): Mono<User> {
        return cacheData(key, user, 60, ChronoUnit.MINUTES)
            .switchIfEmpty(get(key))
    }

    override fun cache(user: User): Mono<User> {
        return cacheData(user.phoneNumber, user, 60, ChronoUnit.MINUTES)
    }

    override fun cacheAll(users: Flux<User>): Flux<User> {
        return users.flatMap { cacheData(it.phoneNumber, it, 60, ChronoUnit.MINUTES) }
    }

    override fun getAll(phones: List<String>): Mono<CacheResult<String, User>> {
        if (phones.isEmpty()) return Mono.empty()

        return redisTemple.opsForValue()
            .multiGet(phones)
            .map { users ->
                val found = users.stream().filter { it != null }
                    .collect(Collectors.toMap(User::phoneNumber, Function.identity()))
                val missing = phones - users.filterNotNull().map(User::phoneNumber).toList().toSet()

                CacheResult(found, missing)
            }
    }
}