/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 5:05 PM-28/07/2025
 *  User: kimin
 **/

package com.lamnguyen.chat.handlers

import com.lamnguyen.chat.domain.request.CreateRoomChatRequest
import com.lamnguyen.chat.services.business.IRoomChatMemberService
import com.lamnguyen.chat.services.business.IRoomChatService
import com.lamnguyen.chat.utils.helpers.ok
import com.lamnguyen.chat.utils.helpers.validate
import org.springframework.security.access.prepost.PreAuthorize
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
    val validator: Validator
) {
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER', 'USER_CREATE_ROOM_CHAT')")
    fun createRoomChat(request: ServerRequest): Mono<ServerResponse?> {
        return request.bodyToMono(CreateRoomChatRequest::class.java)
            .flatMap { body ->
                validator.validate(body) { chatRequest ->
                    roomChatSerVice
                        .createRoomChat(chatRequest.title ?: "")
                        .flatMap { romChat ->
                            Flux.fromIterable(body.members ?: emptyList())
                                .flatMap { member -> roomChatMemberService.addMember(romChat.id ?: 0, member) }
                                .collectList()
                        }
                }
            }.then(ok(""))
    }
}