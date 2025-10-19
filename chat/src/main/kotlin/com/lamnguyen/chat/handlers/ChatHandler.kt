/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 5:05 PM-28/07/2025
 *  User: kimin
 **/

package com.lamnguyen.chat.handlers

import com.fasterxml.uuid.Generators
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
import reactor.kotlin.core.util.function.component1
import reactor.kotlin.core.util.function.component2

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
                chatRequest.members!!.remove(auth.name)
                if (chatRequest.members.isNullOrEmpty()) return@flatMap Mono.error(ApplicationException(ExceptionEnum.LIST_MEMBER_IS_EMPTY))
                chatRequest.adminRoomChat = auth.name
                userGrpcService.getFriendShips(chatRequest.adminRoomChat!!, chatRequest.members!!)
                    .map { it.friendsList }
            }
            .filter { it.isNotEmpty() }
            .switchIfEmpty(Mono.error(ApplicationException(ExceptionEnum.LIST_MEMBER_IS_EMPTY)))
            .flatMap { users ->
                chatRequest.members = users.map { it.phoneNumber }.sorted().toMutableList()
                val roomChatId = Generators.timeBasedEpochGenerator().generate().toString();
                val roomChat = RoomChat().apply {
                    id = roomChatId
                    title = chatRequest.title!!
                    type =
                        if (chatRequest.members?.size == 1) RoomChat.RoomChatType.SINGLE else RoomChat.RoomChatType.GROUP
                    newRow = true
                }
                Mono.zip(
                    roomChatSerVice.createRoomChat(roomChat),
                    addMember(roomChatId, chatRequest)
                )
                    .map { (chat, _) -> chat }
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

    private fun addMember(roomChatId: String, chatRequest: CreateRoomChatRequest): Mono<Boolean> {
        val fluxAddMember = Flux.fromIterable(chatRequest.members!!)
            .flatMap {
                roomChatMemberService.addMember(roomChatId, it)
            }
        val fluxOwner = Flux.from(
            roomChatMemberService
                .addMember(
                    roomChatId,
                    chatRequest.adminRoomChat!!,
                    RoomChatMember.Role.ADMIN
                )
        )

        return Flux.zip(fluxAddMember, fluxOwner)
            .then(Mono.just(true))
            .onErrorResume {
                roomChatSerVice
                    .removeRoomChat(roomChatId)
                    .then(Mono.error(it))
            }
    }
}

