/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 5:13 AM-15/07/2025
 *  User: kimin
 **/

package com.lamnguyen.user.services.business.v1

import com.lamnguyen.user.domain.dto.UserDto
import com.lamnguyen.user.domain.request.RegisInfoRequest
import com.lamnguyen.user.exceptions.ApplicationException
import com.lamnguyen.user.exceptions.ExceptionEnum
import com.lamnguyen.user.mappers.IUserMapper
import com.lamnguyen.user.models.User
import com.lamnguyen.user.repositories.IFriendShipRepository
import com.lamnguyen.user.repositories.IUserRepository
import com.lamnguyen.user.services.business.IUserService
import com.lamnguyen.user.utils.enums.RelationShipStatus
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

    override fun getAllFriend(ownerPhoneNumber: String): Flux<UserDto> {
        return friendRepository.findAllByOwnerPhoneNumber(ownerPhoneNumber)
            .flatMap { friendShip ->
                userRepository.findUserByPhoneNumber(friendShip.friendPhoneNumber)
                    .map {
                        userMapper.toDto(it).apply {
                            displayName = friendShip.displayName
                            relationShipStatus = RelationShipStatus.FRIEND
                        }
                    }
            }
    }

    override fun getInfo(phoneNumber: String): Mono<UserDto> {
        return userRepository.findUserByPhoneNumber(phoneNumber)
            .switchIfEmpty(Mono.error(ApplicationException(ExceptionEnum.USER_NOT_FOUND)))
            .map(userMapper::toDto)
    }

    override fun regisInfo(
        phoneNumber: String,
        data: RegisInfoRequest
    ): Mono<UserDto> {
        val entity = userMapper.toEntity(data).apply {
            this.phoneNumber = phoneNumber
            this.isNewUser = true
        }
        return userRepository.save(entity).map(userMapper::toDto)
    }

    override fun findFriendAndStrangerByPhoneNumber(
        ownerPhoneNumber: String,
        phoneNumber: String
    ): Flux<UserDto> {
        if (phoneNumber.isBlank()) return Flux.empty()

        return userRepository.findFriendAndStrangerByPhoneNumber(ownerPhoneNumber, phoneNumber)
    }

    override fun getInfoAndFriendShip(
        owner: String,
        members: List<String>
    ): Flux<UserDto> {
        return userRepository.findFriendAndStrangerByPhoneNumber(owner, members)
    }
}