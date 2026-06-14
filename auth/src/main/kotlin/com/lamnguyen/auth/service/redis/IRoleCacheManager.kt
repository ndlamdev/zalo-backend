/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 4:05 PM-11/08/2025
 *  User: kimin
 **/

package com.lamnguyen.auth.service.redis

import com.lamnguyen.auth.model.Role
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface IRoleCacheManager {
    fun getRoles(phoneNumber: String): Flux<Role>
    fun cacheRoles(phoneNumber: String, roles: Flux<Role>): Flux<Role>
    fun cleanCache(phoneNumber: String): Mono<Void>
}