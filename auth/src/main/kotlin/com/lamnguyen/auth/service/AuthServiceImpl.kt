/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 6:33 AM-10/07/2025
 *  User: kimin
 **/

package com.lamnguyen.auth.service

import com.lamnguyen.auth.exceptions.ApplicationException
import com.lamnguyen.auth.exceptions.ExceptionEnum
import com.lamnguyen.auth.model.User
import com.lamnguyen.auth.domain.requests.RegisterRequest
import com.lamnguyen.auth.repositories.IUserRepository
import formatPhoneNumber
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class AuthServiceImpl(val userRepository: IUserRepository, val passwordEncoder: PasswordEncoder) : IAuthService {
    override fun register(data: RegisterRequest): Mono<Void> {
        val phoneNumber = formatPhoneNumber(data.phoneNumber)
        if (phoneNumber == null) return Mono.error(ApplicationException(ExceptionEnum.REGISTER_ERROR))
        return userRepository.findByPhoneNumber(phoneNumber)
            .flatMap {
                Mono.error<Void>(ApplicationException(ExceptionEnum.USER_EXISTED))
            }.switchIfEmpty(
                userRepository.save(User().apply {
                    this.phoneNumber = phoneNumber
                    this.password = passwordEncoder.encode(data.password)
                }).then()
            )
    }
}