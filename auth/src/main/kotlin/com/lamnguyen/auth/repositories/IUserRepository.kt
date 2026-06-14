/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 2:36 PM-08/07/2025
 *  User: kimin
 **/

package com.lamnguyen.auth.repositories

import com.lamnguyen.auth.model.User
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface IUserRepository : ReactiveCrudRepository<User, String> {
    fun findByPhoneNumber(phoneNumber: String): Mono<User?>

    fun existsUserByPhoneNumber(phoneNumber: String): Mono<Boolean>

    fun existsUserByPhoneNumberAndActiveIsFalse(phoneNumber: String): Mono<Boolean>

    @Query("""
        UPDATE users u 
        SET u.active = true 
        WHERE u.phone_number = :phoneNumber
    """)
    fun activeUserByPhoneNumber(phoneNumber: String): Mono<Int>
}