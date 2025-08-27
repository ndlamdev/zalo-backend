/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 9:47 AM-01/08/2025
 *  User: kimin
 **/

package com.lamnguyen.chat.services.grpc.v1

import com.lamnguyen.chat.protos.FriendShipCheckRequest
import com.lamnguyen.chat.protos.FriendShipCheckResponse
import com.lamnguyen.chat.protos.UserServiceGrpc
import com.lamnguyen.chat.services.grpc.IUserGrpcService
import com.lamnguyen.chat.utils.annotations.GrpcPreAuthorizeHasAnyAuthority
import com.lamnguyen.chat.utils.helpers.grpcCall
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class UserGrpcServiceImpl(
    val userGrpcService: UserServiceGrpc.UserServiceBlockingStub,
) : IUserGrpcService {

    @GrpcPreAuthorizeHasAnyAuthority("ROLE_USER", "ROLE_ADMIN", "GET_FRIEND_SHIPS")
    override fun getFriendShips(
        adminPhoneNumber: String,
        members: List<String>,
        token: String?,
    ): Mono<FriendShipCheckResponse> {
        return grpcCall(userGrpcService, token)
            .map {
                val request = FriendShipCheckRequest.newBuilder().apply {
                    addAllFriendPhoneNumbers(members)
                }.build()
                it.checkFriendShip(request)
            }.onErrorResume { error -> Mono.error(error) }
    }
}