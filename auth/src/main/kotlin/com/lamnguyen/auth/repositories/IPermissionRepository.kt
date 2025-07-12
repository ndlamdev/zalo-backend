/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 5:32 AM-09/07/2025
 *  User: kimin
 **/

package com.lamnguyen.auth.repositories

import com.lamnguyen.auth.model.Permission
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux

interface IPermissionRepository : ReactiveCrudRepository<Permission, String> {
    @Query("""
        SELECT * FROM permissions p
        left join permissions_of_role pr
            left join roles r on r.name = pr.role_name
         on pr.permission_name = p.name
        where r.name = :name
    """)
    fun findAllByRoleName(role: String): Flux<Permission>
}