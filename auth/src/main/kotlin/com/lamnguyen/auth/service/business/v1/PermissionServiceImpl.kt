/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 10:12 AM-12/08/2025
 *  User: kimin
 **/

package com.lamnguyen.auth.service.business.v1

import com.lamnguyen.auth.model.Permission
import com.lamnguyen.auth.repositories.IPermissionRepository
import com.lamnguyen.auth.service.business.IPermissionService
import com.lamnguyen.auth.service.redis.IPermissionCacheManager
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class PermissionServiceImpl(
    val permissionRepository: IPermissionRepository,
    val permissionCacheManager: IPermissionCacheManager,
) : IPermissionService {
    override fun getPermissions(role: String): Flux<Permission> {
        val permissions = permissionRepository.findAllByRoleName(role)
        return permissionCacheManager.getPermissions(role)
            .switchIfEmpty(
                permissionCacheManager.cachePermissions(
                    role,
                    permissions
                )
            )
    }
}