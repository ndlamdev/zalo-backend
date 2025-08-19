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
import com.lamnguyen.chat.entities.RoomChatMember
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
            }.then(ok(null))
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER', 'USER_GET_ALL_ROOM_CHAT')")
    fun getAllRoomChat(request: ServerRequest): Mono<ServerResponse?> {
        return ReactiveSecurityContextHolder.getContext()
            .flatMapMany { securityContext -> roomChatSerVice.getAllRoomChat(securityContext.authentication.name) }
            .collectList()
            .flatMap { ok(it) }
    }

    private fun createNewRoomChat(chatRequest: CreateRoomChatRequest): Mono<RoomChat> {
        return ReactiveSecurityContextHolder.getContext()
            .flatMap { securityContext ->
                val auth = securityContext.authentication
                chatRequest.members!!.remove(auth.name)
                if (chatRequest.members!!.isEmpty())
                    return@flatMap Mono.error(ApplicationException(ExceptionEnum.LIST_MEMBER_IS_EMPTY))

                return@flatMap userGrpcService
                    .getFriendShips(auth.name, chatRequest.members!!)
                    .map { it.friendsList }
                    .filter { it.isNotEmpty() }
                    .switchIfEmpty(Mono.error(ApplicationException(ExceptionEnum.LIST_MEMBER_IS_EMPTY)))
                    .flatMap { users ->
                        roomChatSerVice
                            .createRoomChat(if (users.size == 1) RoomChat().apply {
                                title = users.first().displayName
                                avatar = users.first().avatar
                            } else RoomChat().apply {
                                title = chatRequest.title!!
                            })
                            .flatMap { romChat ->
                                val fluxAddMember = Flux.fromIterable(users).flatMap { user ->
                                    roomChatMemberService
                                        .addMember(romChat.id ?: 0, user.phoneNumber)
                                        .thenReturn(romChat)
                                }
                                val fluxOwner = Flux.from(
                                    roomChatMemberService
                                        .addMember(romChat.id ?: 0, auth.name, RoomChatMember.Role.ADMIN)
                                        .thenReturn(romChat)
                                )
                                Flux.zip(fluxAddMember, fluxOwner)
                                    .then(Mono.just(romChat))
                                    .onErrorResume { error ->
                                        roomChatSerVice
                                            .removeRoomChat(romChat.id!!)
                                            .then(Mono.error(error))
                                    }
                            }
                    }
            }
    }
}

