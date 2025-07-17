/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 5:13 AM-15/07/2025
 *  User: kimin
 **/

package com.lamnguyen.user.services.business

import com.lamnguyen.user.exceptions.ApplicationException
import com.lamnguyen.user.exceptions.ExceptionEnum
import com.lamnguyen.user.models.User
import com.lamnguyen.user.repositories.IUserRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class UserServiceImpl(val userRepository: IUserRepository) : IUserService {
    override fun createUser(phoneNumber: String): Mono<User> {
        return userRepository.save(User().apply { this.phoneNumber = phoneNumber })
            .onErrorResume {
                Mono.error(ApplicationException(ExceptionEnum.CREATE_USER_FAILED))
            }
    }

    override fun findByPhoneNumber(formatPhoneNumber: String?): Mono<User> {
        return userRepository.findUserByPhoneNumber(formatPhoneNumber ?: "")
    }
}