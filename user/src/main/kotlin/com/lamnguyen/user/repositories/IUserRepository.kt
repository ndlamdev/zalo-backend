/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 5:09 AM-15/07/2025
 *  User: kimin
 **/

package com.lamnguyen.user.repositories

import com.lamnguyen.user.domain.dto.UserDto
import com.lamnguyen.user.models.User
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
interface IUserRepository : R2dbcRepository<User, String> {
    fun findUserByPhoneNumber(phoneNumber: String): Mono<User>

    @Query(
        """
        SELECT IF(fs.friend_phone_number IS NOT NULL, 'FRIEND', 'STRANGER') as relation_ship_status,
            users.*
        FROM users
                 LEFT JOIN friendships fs
                           ON fs.friend_phone_number = users.phone_number
                               AND fs.owner_phone_number = :ownerPhoneNumber
        WHERE (
            (fs.friend_phone_number IS NOT NULL AND users.phone_number LIKE CONCAT('%', :phoneNumber, '%'))
                OR
            (fs.friend_phone_number IS NULL AND users.phone_number = :phoneNumber)
            )
          AND users.phone_number != :ownerPhoneNumber;
    """
    )
    fun findFriendAndStrangerByPhoneNumber(ownerPhoneNumber: String, phoneNumber: String): Flux<UserDto>
}
