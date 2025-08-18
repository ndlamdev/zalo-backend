/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 4:55 PM-30/07/2025
 *  User: kimin
 **/

package com.lamnguyen.user.services.business.v1

import com.lamnguyen.user.models.FriendShip
import com.lamnguyen.user.protos.FriendShipCheck
import com.lamnguyen.user.protos.FriendShipCheckResponse
import com.lamnguyen.user.protos.FriendShipCheckResult
import com.lamnguyen.user.repositories.IFriendShipRepository
import com.lamnguyen.user.repositories.IUserRepository
import com.lamnguyen.user.services.business.IFriendShipService
import formatPhoneNumber
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class FriendShipServiceImpl(
    val friendShipRepository: IFriendShipRepository,
    val userRepository: IUserRepository,
) : IFriendShipService {
    override fun checkFriendShip(friendShipsList: MutableList<FriendShipCheck>): Mono<FriendShipCheckResponse> {
        return Flux.fromIterable(friendShipsList)
            .flatMap { friendShip ->
                friendShipRepository.existsFriendShipsByOwnerPhoneNumberAndFriendPhoneNumber(
                    friendShip.phoneNumberChecker,
                    friendShip.phoneNumberFriend
                )
                    .flatMap { result ->
                        Mono.just(
                            FriendShipCheckResult.newBuilder()
                                .apply {
                                    phoneNumberChecker = formatPhoneNumber(friendShip.phoneNumberChecker)
                                    phoneNumberFriend = formatPhoneNumber(friendShip.phoneNumberFriend)
                                    this.result = result
                                }
                                .build())
                    }
            }.collectList()
            .flatMap { Mono.just(FriendShipCheckResponse.newBuilder().addAllResult(it).build()) }
    }

    override fun getAllFriend(phoneNumber: String): Flux<FriendShip> {
        return friendShipRepository.findAllByOwnerPhoneNumber(formatPhoneNumber(phoneNumber))
    }

    override fun addFriend(
        phoneNumberSender: String,
        phoneNumberReceiver: String
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
}