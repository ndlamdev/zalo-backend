/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 3:33 PM-02/08/2025
 *  User: kimin
 **/

package com.lamnguyen.chatws.utils.redis

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
        data: Supplier<Mono<T>>,
        amount: Long?,
        unit: ChronoUnit?
    ): Mono<T> {
        return redissonClient.getLock("lock:$key")
            .tryLock(10, 5, TimeUnit.SECONDS)
            .flatMap { locked ->
                if (locked) {
                    data.get()
                        .flatMap { dataInDb ->
                            redisTemple.opsForValue()
                                .set(key, dataInDb!!, Duration.of(amount ?: 60, unit ?: ChronoUnit.MINUTES))
                                .map { dataInDb }
                        }
                } else {
                    Mono.delay(Duration.ofMillis(100))
                        .flatMap { getData(key) }
                }
            }
    }

    override fun cacheAllData(
        key: String,
        data: Supplier<Flux<T>>,
        amount: Long?,
        unit: ChronoUnit?
    ): Flux<T> {
        return redissonClient.getLock("lock:$key")
            .tryLock(10, 5, TimeUnit.SECONDS)
            .flatMapMany { locked ->
                if (locked) {
                    data.get()
                        .collectList()
                        .filter { it.isNotEmpty() }
                        .flatMap { dataInDb ->
                            redisTemple.opsForList()
                                .leftPushAll(key, *toArray(dataInDb))
                                .thenReturn(dataInDb)
                                .flatMap {
                                    redisTemple.expire(key, Duration.of(amount ?: 60, unit ?: ChronoUnit.MINUTES))
                                }
                                .thenReturn(dataInDb)
                        }
                        .flatMapMany { Flux.fromIterable(it) }
                } else {
                    Mono.delay(Duration.ofMillis(100))
                        .thenMany(getAllData(key))
                }
            }
    }

    override fun clearCache(key: String) {
        redisTemple.delete(key)
            .subscribe()
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

    @Suppress("UNCHECKED_CAST")
    private fun toArray(list: List<T>): Array<T> {
        val array = arrayOfNulls<Any>(list.size)
        list.forEachIndexed { index, item ->
            array[index] = item
        }

        return array as Array<T>
    }
}