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

/**
 * Định nghĩa nghiệp vụ lấy danh sách quyền theo role.
 */
interface IPermissionService {
    /**
     * Lấy các quyền được gán cho role.
     *
     * @param role tên role cần lấy quyền.
     * @return Flux phát ra danh sách quyền của role.
     */
    fun getPermissions(role: String): Flux<Permission>
}
