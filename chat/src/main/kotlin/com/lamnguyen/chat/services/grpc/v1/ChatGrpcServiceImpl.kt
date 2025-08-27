/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 3:58 AM-26/08/2025
 *  User: kimin
 **/

package com.lamnguyen.chat.services.grpc.v1

import com.lamnguyen.chat.protos.ChatServiceGrpc
import com.lamnguyen.chat.protos.RoomChatMemberRequest
import com.lamnguyen.chat.protos.RoomChatMemberResponse
import com.lamnguyen.chat.services.business.IRoomChatMemberService
import io.grpc.stub.StreamObserver
import net.devh.boot.grpc.server.service.GrpcService

@GrpcService
class ChatGrpcServiceImpl(
    val roomChatMemberService: IRoomChatMemberService,
) : ChatServiceGrpc.ChatServiceImplBase() {
    override fun getRoomChatMember(
        request: RoomChatMemberRequest?,
        responseObserver: StreamObserver<RoomChatMemberResponse?>?,
    ) {
        roomChatMemberService.getMembersInRoomChat(request?.roomChatId!!)
            .collectList()
            .map { it.map { member -> member.phoneNumber } }
            .doOnSuccess {
                val response = RoomChatMemberResponse.newBuilder().apply {
                    addAllPhoneNumber(it)
                }.build()
                responseObserver?.onNext(response)
                responseObserver?.onCompleted()
            }.subscribe()
    }
}