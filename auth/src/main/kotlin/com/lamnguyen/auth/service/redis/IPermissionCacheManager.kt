/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 4:05 PM-11/08/2025
 *  User: kimin
 **/

package com.lamnguyen.auth.service.redis

import com.lamnguyen.auth.model.Permission
import reactor.core.publisher.Flux

interface IPermissionCacheManager {
    fun getPermissions(role: String): Flux<Permission>
    fun cachePermissions(role: String, permissions: Flux<Permission>): Flux<Permission>
    fun cleanPermissions(role: String)
}