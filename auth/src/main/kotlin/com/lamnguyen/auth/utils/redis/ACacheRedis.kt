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

abstract class ACacheRedis<T>(
    val redissonClient: RedissonReactiveClient,
    val redisTemple: ReactiveRedisTemplate<String, T>,
) : ICacheRedis<T> {
    protected fun executeMono(
        key: String,
        waitTime: Long = 10,
        leaseTime: Long = 5,
        timeUnit: TimeUnit = TimeUnit.SECONDS,
        action: (locked: Boolean) -> Mono<T>
    ): Mono<T> {
        val lock = redissonClient.getLock(key)

        return lock.tryLock(waitTime, leaseTime, timeUnit)
            .flatMap { action(it) }
            .doFinally {
                lock.isLocked
                    .filter { it }
                    .flatMap { lock.unlock() }
                    .onErrorResume { Mono.empty() }
                    .subscribe()
            }
    }

    protected fun executeFlux(
        key: String,
        waitTime: Long = 10,
        leaseTime: Long = 5,
        timeUnit: TimeUnit = TimeUnit.SECONDS,
        action: (locked: Boolean) -> Flux<T>
    ): Flux<T> {
        val lock = redissonClient.getLock(key)

        return lock.tryLock(waitTime, leaseTime, timeUnit)
            .flatMapMany { action(it) }
            .doFinally {
                lock.isLocked
                    .filter { it }
                    .flatMap { lock.unlock() }
                    .onErrorResume { Mono.empty() }
                    .subscribe()
            }
    }

    override fun cacheData(
        key: String,
        data: Mono<T>,
        amount: Long?,
        unit: ChronoUnit?
    ): Mono<T> {
        return executeMono("lock:$key") { lock ->
            if (!lock) return@executeMono Mono.delay(Duration.ofMillis(100))
                .flatMap { getData(key) }

            data.flatMap { dataInDb ->
                if (amount == null || unit == null) redisTemple.opsForValue()
                    .set(key, dataInDb!!)
                    .map { dataInDb }
                else redisTemple.opsForValue()
                    .set(key, dataInDb!!, Duration.of(amount, unit))
                    .map { dataInDb }
            }
        }
    }

    override fun cacheAllData(
        key: String,
        data: Flux<T>,
        amount: Long?,
        unit: ChronoUnit?
    ): Flux<T> {
        return executeFlux("lock:$key") { lock ->
            if (!lock) return@executeFlux Mono.delay(Duration.ofMillis(100))
                .thenMany(getAllData(key))

            data.collectList()
                .filter { it.isNotEmpty() }
                .flatMap { dataInDb ->
                    redisTemple.opsForList()
                        .leftPushAll(key, dataInDb)
                        .flatMap {
                            if (amount == null || unit == null) Mono.empty()
                            else redisTemple.expire(key, Duration.of(amount, unit))
                        }
                        .thenReturn(dataInDb)
                }.flatMapMany { Flux.fromIterable(it) }
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
        val result = redisTemple.opsForList()
            .range(key, 0, -1)
        if (expire ?: true)
            result.flatMap { data ->
                redisTemple.expire(key, Duration.of(amount ?: 60, unit ?: ChronoUnit.MINUTES))
                    .thenReturn(data!!)
            }

        return result
    }
}