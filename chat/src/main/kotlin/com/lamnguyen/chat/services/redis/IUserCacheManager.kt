package com.lamnguyen.chat.services.redis

import com.lamnguyen.chat.domain.dto.UserDto
import com.lamnguyen.chat.utils.redis.CacheResult
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface IUserCacheManager {
    fun cache(user: Mono<UserDto>): Mono<UserDto>
    fun cache(user: UserDto): Mono<UserDto>
    fun cacheAll(users: Flux<UserDto>): Flux<UserDto>
    fun get(phone: String): Mono<UserDto>
    fun getAll(phones: List<String>): Mono<CacheResult<String, UserDto>>
}