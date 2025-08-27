/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 9:46 AM-01/08/2025
 *  User: kimin
 **/

package com.lamnguyen.chat.services.grpc

import com.lamnguyen.chat.protos.FriendShipCheckResponse
import reactor.core.publisher.Mono

interface IUserGrpcService {
    fun getFriendShips(
        adminPhoneNumber: String,
        members: List<String>,
        token: String? = null,
    ): Mono<FriendShipCheckResponse>
}