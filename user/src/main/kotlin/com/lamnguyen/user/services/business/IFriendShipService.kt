/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 4:55 PM-30/07/2025
 *  User: kimin
 **/

package com.lamnguyen.user.services.business

import com.lamnguyen.user.protos.FriendShipCheck
import com.lamnguyen.user.protos.FriendShipCheckResponse
import reactor.core.publisher.Mono

interface IFriendShipService {
    fun checkFriendShip(friendShipsList: MutableList<FriendShipCheck>): Mono<FriendShipCheckResponse>
}