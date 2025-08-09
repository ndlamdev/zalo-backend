/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 5:12 AM-15/07/2025
 *  User: kimin
 **/

package com.lamnguyen.user.services.business

import com.lamnguyen.user.models.User
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface IUserService {
    fun createUser(phoneNumber: String): Mono<User>
    fun findByPhoneNumber(formatPhoneNumber: String?): Mono<User>
}