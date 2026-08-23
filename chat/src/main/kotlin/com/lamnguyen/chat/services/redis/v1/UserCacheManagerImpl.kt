package com.lamnguyen.chat.services.redis.v1

import com.lamnguyen.chat.domain.dto.UserDto
import com.lamnguyen.chat.services.redis.IUserCacheManager
import com.lamnguyen.chat.utils.redis.ACacheRedis
import com.lamnguyen.chat.utils.redis.CacheResult
import com.lamnguyen.chat.utils.redis.ICacheRedis
import org.redisson.api.RedissonReactiveClient
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.temporal.ChronoUnit
import java.util.function.Function
import java.util.stream.Collectors

@Component
class UserCacheManagerImpl(
    redissonClient: RedissonReactiveClient,
    redisTemple: ReactiveRedisTemplate<String, UserDto>,
) : IUserCacheManager, ACacheRedis<UserDto>(redissonClient, redisTemple) {
    override fun cache(user: Mono<UserDto>): Mono<UserDto> {
        return user.flatMap { cacheData(key(it.phoneNumber), it, 60, ChronoUnit.MINUTES) }
    }

    override fun cache(user: UserDto): Mono<UserDto> {
        return cacheData(key(user.phoneNumber), user, 60, ChronoUnit.MINUTES)
    }

    override fun cacheAll(users: Flux<UserDto>): Flux<UserDto> {
        return users.flatMap { cacheData(key(it.phoneNumber), it, 60, ChronoUnit.MINUTES) }
    }

    override fun get(phone: String): Mono<UserDto> {
        return super.getData(key(phone), true, null, null)
    }

    override fun getAll(phones: List<String>): Mono<CacheResult<String, UserDto>> {
        if (phones.isEmpty()) return Mono.empty()

        return redisTemple.opsForValue()
            .multiGet(phones.map(this::key))
            .map { users ->
                val found = users.stream().filter { it != null }
                    .collect(Collectors.toMap(UserDto::phoneNumber, Function.identity()))
                val missing = phones - users.filterNotNull().map(UserDto::phoneNumber).toList().toSet()

                CacheResult(found, missing)
            }
    }

    private fun key(phone: String): String {
        return ICacheRedis.hashKeys("User", phone)
    }
}