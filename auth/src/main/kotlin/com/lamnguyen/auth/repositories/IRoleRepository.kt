/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 2:37 PM-08/07/2025
 *  User: kimin
 **/

package com.lamnguyen.auth.repositories

import com.lamnguyen.auth.model.entity.Role
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
interface IRoleRepository : ReactiveCrudRepository<Role, Long> {
    @Query(
        """
        SELECT * FROM roles r
        left join roles_of_user ru 
            left join users u on u.phone_number = ru.user_phone_number
        on ru.role_name = r.name
        where u.phone_number = :phoneNumber
    """
    )
    fun findByUserPhoneNumber(phoneNumber: String): Flux<Role?>
}