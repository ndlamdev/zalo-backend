/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 3:33 PM-02/08/2025
 *  User: kimin
 **/

package com.lamnguyen.auth.utils.redis

import org.redisson.api.RedissonReactiveClient
import org.springframework.data.redis.core.ReactiveRedisTemplate
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit
import java.util.function.Supplier

abstract class ACacheRedis<T>(
    val redissonClient: RedissonReactiveClient,
    val redisTemple: ReactiveRedisTemplate<String, T>,
) : ICacheRedis<T> {
    override fun cacheData(
        key: String,
        data: Mono<T>,
        amount: Long?,
        unit: ChronoUnit?
    ): Mono<T> {
        val locker = redissonClient.getLock("lock:$key")
        return locker
            .tryLock(10, 5, TimeUnit.SECONDS)
            .flatMap { locked ->
                if (!locked) return@flatMap Mono.delay(Duration.ofMillis(100))
                    .flatMap { getData(key) }

                Mono.usingWhen(Mono.just(locker), {
                    data.flatMap { dataInDb ->
                        if (amount == null || unit == null) return@flatMap redisTemple.opsForValue()
                            .set(key, dataInDb!!)
                            .map { dataInDb }

                        return@flatMap redisTemple.opsForValue()
                            .set(key, dataInDb!!, Duration.of(amount, unit))
                            .map { dataInDb }
                    }
                }, { it.unlock() })
            }
    }

    override fun cacheAllData(
        key: String,
        data: Flux<T>,
        amount: Long?,
        unit: ChronoUnit?
    ): Flux<T> {
        val locker = redissonClient.getLock("lock:$key")
        return locker.tryLock(10, 5, TimeUnit.SECONDS)
            .flatMapMany { lock ->
                if (!lock) return@flatMapMany Mono.delay(Duration.ofMillis(100))
                    .thenMany(getAllData(key))

                Flux.usingWhen(Mono.just(locker), {
                    data.collectList()
                        .filter { it.isNotEmpty() }
                        .flatMap { dataInDb ->
                            redisTemple.opsForList()
                                .leftPushAll(key, dataInDb)
                                .flatMap {
                                    if (amount == null || unit == null) return@flatMap Mono.empty()
                                    redisTemple.expire(key, Duration.of(amount, unit))
                                }
                                .thenReturn(dataInDb)
                        }.flatMapMany { Flux.fromIterable(it) }
                }, { it.unlock() })
            }
    }

    override fun clearCache(key: String): Mono<Long> {
        return redisTemple.delete(key)
    }

    override fun getData(
        key: String,
        expire: Boolean?,
        amount: Long?,
        unit: ChronoUnit?
    ): Mono<T> {
        if (expire ?: true)
            return redisTemple.opsForValue()
                .getAndExpire(
                    key,
                    Duration.of(amount ?: 60, unit ?: ChronoUnit.MINUTES)
                )

        return redisTemple.opsForValue().get(key)
    }

    override fun getAllData(
        key: String,
        expire: Boolean?,
        amount: Long?,
        unit: ChronoUnit?
    ): Flux<T> {
        if (expire ?: true)
            return redisTemple.opsForList()
                .range(key, 0, -1)
                .flatMap { data ->
                    redisTemple.expire(key, Duration.of(amount ?: 60, unit ?: ChronoUnit.MINUTES))
                        .thenReturn(data!!)
                }

        return redisTemple.opsForList().range(key, 0, -1)
    }
}