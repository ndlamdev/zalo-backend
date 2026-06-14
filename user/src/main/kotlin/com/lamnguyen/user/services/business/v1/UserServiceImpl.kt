/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 5:13 AM-15/07/2025
 *  User: kimin
 **/

package com.lamnguyen.user.services.business.v1

import com.lamnguyen.user.domain.dto.UserDto
import com.lamnguyen.user.exceptions.ApplicationException
import com.lamnguyen.user.exceptions.ExceptionEnum
import com.lamnguyen.user.mappers.IUserMapper
import com.lamnguyen.user.models.User
import com.lamnguyen.user.repositories.IFriendShipRepository
import com.lamnguyen.user.repositories.IUserRepository
import com.lamnguyen.user.services.business.IUserService
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class UserServiceImpl(
    val userRepository: IUserRepository,
    val friendRepository: IFriendShipRepository,
    val userMapper: IUserMapper,
) : IUserService {
    override fun createUser(phoneNumber: String): Mono<User> {
        return userRepository.save(
            User().apply {
                this.phoneNumber = phoneNumber
                this.isNewUser = true
            }
        )
            .onErrorResume {
                Mono.error(ApplicationException(ExceptionEnum.CREATE_USER_FAILED))
            }
    }

    override fun findByPhoneNumber(formatPhoneNumber: String): Mono<User> {
        return userRepository.findUserByPhoneNumber(formatPhoneNumber)
    }

    override fun getAllFriend(ownerPhoneNumber: String): Flux<UserDto> {
        return friendRepository.findAllByOwnerPhoneNumber(ownerPhoneNumber)
            .flatMap { friendShip ->
                userRepository.findUserByPhoneNumber(friendShip.friendPhoneNumber)
                    .map {
                        userMapper.toDto(it).apply {
                            displayName = friendShip.displayName
                            isFriend = true
                            addFriendRequested = false
                        }
                    }
            }
    }

    override fun getInfo(phoneNumber: String): Mono<UserDto> {
        return userRepository.findUserByPhoneNumber(phoneNumber)
            .map { userMapper.toDto(it) }
    }
}