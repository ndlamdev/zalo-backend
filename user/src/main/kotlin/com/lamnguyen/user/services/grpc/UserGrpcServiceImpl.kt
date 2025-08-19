/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 4:12 PM-30/07/2025
 *  User: kimin
 **/

package com.lamnguyen.user.services.grpc

import com.lamnguyen.user.protos.FriendShipCheckRequest
import com.lamnguyen.user.protos.FriendShipCheckResponse
import com.lamnguyen.user.protos.UserServiceGrpc
import com.lamnguyen.user.services.business.IUserService
import com.lamnguyen.user.utils.annotation.GrpcPreAuthorizeHasAnyAuthority
import io.grpc.stub.StreamObserver
import net.devh.boot.grpc.server.service.GrpcService
import org.springframework.security.core.context.SecurityContextHolder

@GrpcService
class UserGrpcServiceImpl(
    val userService: IUserService,
) : UserServiceGrpc.UserServiceImplBase() {

    @GrpcPreAuthorizeHasAnyAuthority("ROLE_USER", "ROLE_ADMIN", "USER_CHECK_FRIEND_SHIP")
    override fun checkFriendShip(
        request: FriendShipCheckRequest,
        responseObserver: StreamObserver<FriendShipCheckResponse>,
    ) {
        userService.getAllFriend(
            SecurityContextHolder.getContext().authentication.name!!,
            request.friendPhoneNumbersList
        )
            .subscribe {
                responseObserver.onNext(it)
                responseObserver.onCompleted()
            }
    }
}