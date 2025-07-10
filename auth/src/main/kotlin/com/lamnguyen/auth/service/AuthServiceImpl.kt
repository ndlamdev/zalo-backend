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
import com.lamnguyen.auth.model.requests.RegisterRequest
import com.lamnguyen.auth.repositories.IUserRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class AuthServiceImpl(val userRepository: IUserRepository) : IAuthService {
    override fun register(data: RegisterRequest): Mono<Void> {
        return Mono.error(ApplicationException(ExceptionEnum.REGISTER_ERROR))
    }
}