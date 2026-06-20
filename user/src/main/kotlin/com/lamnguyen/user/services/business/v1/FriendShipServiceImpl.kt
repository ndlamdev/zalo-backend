/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 4:55 PM-30/07/2025
 *  User: kimin
 **/

package com.lamnguyen.user.services.business.v1

import com.lamnguyen.user.models.FriendShip
import com.lamnguyen.user.repositories.IFriendShipRepository
import com.lamnguyen.user.repositories.IUserRepository
import com.lamnguyen.user.services.business.IFriendShipService
import com.lamnguyen.user.utils.helpers.formatPhoneNumber
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class FriendShipServiceImpl(
    val friendShipRepository: IFriendShipRepository,
    val userRepository: IUserRepository,
) : IFriendShipService {
    override fun getAllFriend(phoneNumber: String): Flux<FriendShip> {
        return friendShipRepository.findAllByOwnerPhoneNumber(formatPhoneNumber(phoneNumber))
    }

    override fun addFriend(
        phoneNumberSender: String,
        phoneNumberReceiver: String,
    ): Mono<FriendShip> {
        return friendShipRepository
            .existsFriendShipsByOwnerPhoneNumberAndFriendPhoneNumber(phoneNumberSender, phoneNumberReceiver)
            .filter { !it }
            .flatMap {
                Mono.zip(
                    userRepository.findUserByPhoneNumber(phoneNumberSender),
                    userRepository.findUserByPhoneNumber(phoneNumberReceiver)
                )
            }.flatMap {
                val sender = it.t1
                val receiver = it.t2
                Mono.zip(
                    friendShipRepository.save(FriendShip().apply {
                        ownerPhoneNumber = phoneNumberSender
                        friendPhoneNumber = phoneNumberReceiver
                        displayName = receiver.fullName
                    }),
                    friendShipRepository.save(FriendShip().apply {
                        ownerPhoneNumber = phoneNumberReceiver
                        friendPhoneNumber = phoneNumberSender
                        displayName = sender.fullName
                    })
                )
            }.map { it.t1 }
    }

    override fun existsFriendship(
        ownerPhoneNumber: String,
        friendPhoneNumber: String,
    ): Mono<Boolean> {
        return friendShipRepository
            .existsFriendShipsByOwnerPhoneNumberAndFriendPhoneNumber(ownerPhoneNumber, friendPhoneNumber)
    }
}