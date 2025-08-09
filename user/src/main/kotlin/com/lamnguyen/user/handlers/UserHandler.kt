/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 7:29 PM-15/07/2025
 *  User: kimin
 **/

package com.lamnguyen.user.handlers

import com.lamnguyen.user.domain.dto.UserDto
import com.lamnguyen.user.domain.request.InviteAddFriendRequest
import com.lamnguyen.user.mappers.IUserMapper
import com.lamnguyen.user.models.InviteAddFriend
import com.lamnguyen.user.protos.FriendShipCheck
import com.lamnguyen.user.protos.FriendShipCheckResponse
import com.lamnguyen.user.protos.FriendShipCheckResult
import com.lamnguyen.user.services.business.IFriendShipService
import com.lamnguyen.user.services.business.IInviteAddFriendService
import com.lamnguyen.user.services.business.IUserService
import com.lamnguyen.user.utils.helpers.ok
import com.lamnguyen.user.utils.helpers.validate
import formatPhoneNumber
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.core.context.SecurityContext
import org.springframework.stereotype.Component
import org.springframework.validation.Validator
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class UserHandler(
    val userService: IUserService,
    val friendShipService: IFriendShipService,
    val inviteAddFriendService: IInviteAddFriendService,
    val userMapper: IUserMapper,
    val validator: Validator
) {

    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'USER_SEARCH_BY_PHONE_NUMBER')")
    fun search(request: ServerRequest): Mono<ServerResponse?> {
        val phoneNumber = formatPhoneNumber(request.queryParam("phone_number").orElse(""))
        return ReactiveSecurityContextHolder.getContext()
            .flatMap { context ->
                val monoUser = userService.findByPhoneNumber(phoneNumber)
                    .map(userMapper::toDto)
                if (phoneNumber == context.authentication.name) {
                    return@flatMap monoUser.flatMap {
                        it.isFriend = true
                        it.addFriendRequested = false
                        ok(it)
                    }
                }
                checkFriendAndInviteAddFriend(monoUser, context)
                    .flatMap { ok(it) }
                    .switchIfEmpty(ok(null))
            }
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'USER_ADD_FRIEND')")
    fun addFriend(request: ServerRequest): Mono<ServerResponse?> {
        return request
            .bodyToMono(InviteAddFriendRequest::class.java)
            .flatMap {
                validator.validate(it) { request ->
                    inviteAddFriendService.sendRequest(
                        request.phoneNumber,
                        request.message
                    )
                }
            }.then(ok(null))
    }

    private fun checkFriendAndInviteAddFriend(monoUser: Mono<UserDto>, context: SecurityContext): Mono<UserDto> {
        return monoUser
            .flatMap { user ->
                val monoFriend = friendShipService.checkFriendShip(
                    mutableListOf(
                        FriendShipCheck.newBuilder()
                            .setPhoneNumberChecker(context.authentication.name)
                            .setPhoneNumberFriend(user.phoneNumber)
                            .build()
                    )
                ).defaultIfEmpty(
                    FriendShipCheckResponse.newBuilder()
                        .addResult(
                            FriendShipCheckResult.newBuilder()
                                .setPhoneNumberChecker(context.authentication.name)
                                .setPhoneNumberFriend(user.phoneNumber)
                                .setResult(context.authentication.name == user.phoneNumber)
                                .build()
                        )
                        .build()
                )
                val monoInviteAddFriend = inviteAddFriendService.findInviteAddFriend(
                    context.authentication.name,
                    user.phoneNumber
                ).defaultIfEmpty(
                    InviteAddFriend().apply {
                        phoneNumberSender = ""
                        phoneNumberReceiver = ""
                    }
                )
                Mono.zip(
                    monoFriend,
                    monoInviteAddFriend
                )
                    .map { it ->
                        val friend = it.t1
                        val invite = it.t2
                        user.isFriend = friend.resultList.first().result
                        user.addFriendRequested = !invite.phoneNumberSender.isEmpty()
                        user
                    }
            }
    }
}