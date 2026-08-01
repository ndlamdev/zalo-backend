/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 3:32 PM-02/08/2025
 *  User: kimin
 **/

package com.ndlamdev.chatwsrouter.utils.redis

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.temporal.ChronoUnit

interface ICacheRedis<T> {
    fun cacheData(
        key: String,
        data: Mono<T>,
        amount: Long? = 60,
        unit: ChronoUnit? = ChronoUnit.MINUTES
    ): Mono<T>

    fun cacheAllData(
        key: String,
        data: Flux<T>,
        amount: Long? = 60,
        unit: ChronoUnit? = ChronoUnit.MINUTES
    ): Flux<T>

    fun getData(
        key: String,
        expire: Boolean? = true,
        amount: Long? = 60,
        unit: ChronoUnit? = ChronoUnit.MINUTES
    ): Mono<T>

    fun getAllData(
        key: String,
        expire: Boolean? = true,
        amount: Long? = 60,
        unit: ChronoUnit? = ChronoUnit.MINUTES
    ): Flux<T>

    fun clearCache(key: String)

    companion object {
        @JvmStatic
        fun hashKeys(vararg keys: String): String {
            return keys.joinToString(":")
        }

        @JvmStatic
        fun joinKeys(vararg keys: String): String {
            return keys.joinToString("-")
        }
    }
}