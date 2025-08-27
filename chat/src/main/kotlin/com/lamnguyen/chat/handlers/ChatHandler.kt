/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 5:05 PM-28/07/2025
 *  User: kimin
 **/

package com.lamnguyen.chat.handlers

import com.lamnguyen.chat.domain.requests.CreateRoomChatRequest
import com.lamnguyen.chat.entities.RoomChat
import com.lamnguyen.chat.entities.RoomChatMember
import com.lamnguyen.chat.exceptions.ApplicationException
import com.lamnguyen.chat.exceptions.ExceptionEnum
import com.lamnguyen.chat.services.business.IRoomChatMemberService
import com.lamnguyen.chat.services.business.IRoomChatService
import com.lamnguyen.chat.services.grpc.IUserGrpcService
import com.lamnguyen.chat.services.kafka.IRoomChatMemberProducer
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
import java.util.*

@Component
class ChatHandler(
    val roomChatSerVice: IRoomChatService,
    val roomChatMemberService: IRoomChatMemberService,
    val validator: Validator,
    val userGrpcService: IUserGrpcService,
    val roomChatMemberProducer: IRoomChatMemberProducer,
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
            .map { it.authentication }
            .flatMap { auth ->
                if (chatRequest.members.isNullOrEmpty()) return@flatMap Mono.error(ApplicationException(ExceptionEnum.LIST_MEMBER_IS_EMPTY))
                if (chatRequest.members!!.size == 1) return@flatMap Mono.empty()
                chatRequest.members!!.remove(auth.name)
                chatRequest.adminRoomChat = auth.name
                userGrpcService.getFriendShips(chatRequest.adminRoomChat!!, chatRequest.members!!)
                    .map { it.friendsList }
            }
            .filter { it.size > 1 }
            .switchIfEmpty(Mono.empty())
            .flatMap { users ->
                chatRequest.members = users.map { it.phoneNumber }.sorted().toMutableList()
                val roomChat = RoomChat().apply {
                    id = UUID.randomUUID().toString()
                    title = chatRequest.title!!
                    type = RoomChat.RoomChatType.GROUP
                    softId = chatRequest.members!!.joinToString { "_" }
                    newRow = true
                }
                roomChatSerVice.createRoomChat(roomChat)
                    .flatMap { addMember(chatRequest, it) }
            }
            .doOnSuccess {
                roomChatMemberProducer.dumpRoomChatMember(
                    it.id!!,
                    chatRequest.members?.apply {
                        add(chatRequest.adminRoomChat!!)
                    }!!
                )
            }
    }

    private fun addMember(chatRequest: CreateRoomChatRequest, roomChat: RoomChat): Mono<RoomChat> {
        val fluxAddMember = Flux.fromIterable(chatRequest.members!!)
            .flatMap {
                roomChatMemberService.addMember(roomChat.id!!, it)
            }
        val fluxOwner = Flux.from(
            roomChatMemberService
                .addMember(
                    roomChat.id!!,
                    chatRequest.adminRoomChat!!,
                    RoomChatMember.Role.ADMIN
                )
        )

        return Flux.zip(fluxAddMember, fluxOwner)
            .then(Mono.just(roomChat))
            .onErrorResume {
                roomChatSerVice
                    .removeRoomChat(roomChat.id!!)
                    .then(Mono.error(it))
            }
    }
}

