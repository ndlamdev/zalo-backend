/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 4:55 PM-30/07/2025
 *  User: kimin
 **/

package com.lamnguyen.user.services.business

import com.lamnguyen.user.models.FriendShip
import com.lamnguyen.user.protos.FriendShipCheck
import com.lamnguyen.user.protos.FriendShipCheckResponse
import com.lamnguyen.user.protos.FriendShipCheckResult
import com.lamnguyen.user.repositories.IFriendShipRepository
import formatPhoneNumber
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class FriendShipServiceImpl(
    val friendShipRepository: IFriendShipRepository
) : IFriendShipService {
    override fun checkFriendShip(friendShipsList: MutableList<FriendShipCheck>): Mono<FriendShipCheckResponse> {
        return Flux.fromIterable(friendShipsList)
            .flatMap { friendShip ->
                friendShipRepository.existsFriendShips(friendShip.phoneNumberChecker, friendShip.phoneNumberFriend)
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
        return friendShipRepository.findAllByPhoneNumber(formatPhoneNumber(phoneNumber))
    }
}