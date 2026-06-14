/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 10:11 AM-12/08/2025
 *  User: kimin
 **/

package com.lamnguyen.auth.service.business

import com.lamnguyen.auth.model.Role
import reactor.core.publisher.Flux

/**
 * Định nghĩa nghiệp vụ lấy danh sách role của người dùng.
 */
interface IRoleService {
    /**
     * Lấy các role được gán cho người dùng theo số điện thoại.
     *
     * @param phoneNumber số điện thoại của người dùng.
     * @return Flux phát ra danh sách role của người dùng.
     */
    fun getRoles(phoneNumber: String): Flux<Role>
}
