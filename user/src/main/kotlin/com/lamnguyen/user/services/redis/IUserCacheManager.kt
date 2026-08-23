package com.lamnguyen.user.services.redis

import com.lamnguyen.user.models.User
import com.lamnguyen.user.utils.redis.CacheResult
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface IUserCacheManager {
    fun get(phone: String): Mono<User>
    fun cache(key: String, user: Mono<User>): Mono<User>
    fun cache(user: User): Mono<User>
    fun cacheAll(users: Flux<User>): Flux<User>
    fun getAll(phones: List<String>): Mono<CacheResult<String, User>>
}