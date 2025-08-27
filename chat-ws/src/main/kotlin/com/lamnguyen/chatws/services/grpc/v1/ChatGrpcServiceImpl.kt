/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 5:38 PM-26/08/2025
 *  User: kimin
 **/

package com.lamnguyen.chatws.services.grpc.v1

import com.lamnguyen.chatws.protos.ChatServiceGrpc
import com.lamnguyen.chatws.protos.RoomChatMemberRequest
import com.lamnguyen.chatws.services.grpc.IChatGrpcService
import com.lamnguyen.chatws.utils.helpers.grpcCall
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class ChatGrpcServiceImpl(
    val chatGrpcService: ChatServiceGrpc.ChatServiceBlockingStub,
) : IChatGrpcService {
    override fun getMembersInRoomChat(roomChatId: String, token: String?): Mono<List<String>> {
        return grpcCall(chatGrpcService, token)
            .map {
                it.getRoomChatMember(RoomChatMemberRequest.newBuilder().setRoomChatId(roomChatId).build()).phoneNumberList
            }

    }
}