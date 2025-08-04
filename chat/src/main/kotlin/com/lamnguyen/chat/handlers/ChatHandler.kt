/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 5:05 PM-28/07/2025
 *  User: kimin
 **/

package com.lamnguyen.chat.handlers

import com.lamnguyen.chat.domain.request.CreateRoomChatRequest
import com.lamnguyen.chat.entities.RoomChat
import com.lamnguyen.chat.exceptions.ApplicationException
import com.lamnguyen.chat.exceptions.ExceptionEnum
import com.lamnguyen.chat.services.business.IRoomChatMemberService
import com.lamnguyen.chat.services.business.IRoomChatService
import com.lamnguyen.chat.services.grpc.IUserGrpcService
import com.lamnguyen.chat.utils.helpers.ok
import com.lamnguyen.chat.utils.helpers.validate
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.validation.Validator
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Component
class ChatHandler(
    val roomChatSerVice: IRoomChatService,
    val roomChatMemberService: IRoomChatMemberService,
    val validator: Validator,
    val userGrpcService: IUserGrpcService,
) {
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER', 'USER_CREATE_ROOM_CHAT')")
    fun createRoomChat(request: ServerRequest): Mono<ServerResponse?> {
        return request.bodyToMono(CreateRoomChatRequest::class.java)
            .flatMap { body ->
                validator.validate(body) {
                    createNewRoomChat(it)
                }
            }.then(ok(""))
    }

    private fun createNewRoomChat(chatRequest: CreateRoomChatRequest): Mono<RoomChat> {
        return ReactiveSecurityContextHolder.getContext()
            .flatMap { securityContext ->
                val auth = securityContext.authentication
                chatRequest.members!!.remove(auth.name)
                if (chatRequest.members!!.isEmpty())
                    return@flatMap Mono.error(ApplicationException(ExceptionEnum.LIST_MEMBER_IS_EMPTY))

                return@flatMap roomChatSerVice
                    .createRoomChat(chatRequest.title ?: "")
                    .flatMap { romChat ->
                        userGrpcService
                            .getFriendShips(auth.name, chatRequest.members!!)
                            .flatMap { response ->
                                Flux.fromIterable(response.resultList)
                                    .filter { it.result }
                                    .flatMap { roomChatMemberService.addMember(romChat.id ?: 0, it.phoneNumberFriend) }
                                    .switchIfEmpty(Mono.error(ApplicationException(ExceptionEnum.LIST_MEMBER_IS_EMPTY)))
                                    .collectList()
                            }.flatMap {
                                roomChatMemberService
                                    .addMember(romChat.id ?: 0, auth.name)
                                    .thenReturn(romChat)
                            }
                            .onErrorResume {
                                roomChatSerVice
                                    .removeRoomChat(romChat.id!!)
                                    .then(Mono.error { it })
                            }
                    }
            }
    }
}

