/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 10:11 AM-12/08/2025
 *  User: kimin
 **/

package com.lamnguyen.auth.service.business

import com.lamnguyen.auth.model.Permission
import reactor.core.publisher.Flux

interface IPermissionService {
    fun getPermissions(role: String): Flux<Permission>
}