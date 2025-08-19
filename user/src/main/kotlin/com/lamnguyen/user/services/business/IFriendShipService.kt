/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 4:55 PM-30/07/2025
 *  User: kimin
 **/

package com.lamnguyen.user.services.business

import com.lamnguyen.user.models.FriendShip
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface IFriendShipService {
    fun getAllFriend(phoneNumber: String): Flux<FriendShip>
    fun addFriend(phoneNumberSender: String, phoneNumberReceiver: String): Mono<FriendShip>
    fun existsFriendship(ownerPhoneNumber: String, friendPhoneNumber: String): Mono<Boolean>
}