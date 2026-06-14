/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 10:12 AM-12/08/2025
 *  User: kimin
 **/

package com.lamnguyen.auth.service.business.v1

import com.lamnguyen.auth.model.Role
import com.lamnguyen.auth.repositories.IRoleRepository
import com.lamnguyen.auth.service.business.IRoleService
import com.lamnguyen.auth.service.redis.IRoleCacheManager
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

/**
 * Triển khai nghiệp vụ lấy role của người dùng với cơ chế cache Redis.
 *
 * Service ưu tiên đọc role từ cache. Khi cache trống, dữ liệu được lấy từ repository
 * rồi lưu lại vào cache để giảm tải truy vấn database.
 */
@Service
class RoleServiceImpl(
    val roleRepository: IRoleRepository,
    val roleCacheManager: IRoleCacheManager,
) : IRoleService {
    /**
     * Lấy danh sách role của người dùng theo số điện thoại từ cache hoặc repository.
     */
    override fun getRoles(phoneNumber: String): Flux<Role> {
        val roles = roleRepository.findByUserPhoneNumber(phoneNumber)
        return roleCacheManager.getRoles(phoneNumber)
            .switchIfEmpty(
                roleCacheManager.cacheRoles(
                    phoneNumber,
                    roles
                )
            )
    }
}
