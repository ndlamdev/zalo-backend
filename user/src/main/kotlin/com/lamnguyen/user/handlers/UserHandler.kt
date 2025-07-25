/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 7:29 PM-15/07/2025
 *  User: kimin
 **/

package com.lamnguyen.user.handlers

import com.lamnguyen.user.domain.request.InviteAddFriendRequest
import com.lamnguyen.user.domain.request.PhoneNumberRequest
import com.lamnguyen.user.services.business.IInviteAddFriendService
import com.lamnguyen.user.services.business.IUserService
import com.lamnguyen.user.services.business.InviteAddFriendServiceImpl
import com.lamnguyen.user.utils.helpers.ok
import com.lamnguyen.user.utils.helpers.validate
import formatPhoneNumber
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.validation.Validator
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class UserHandler(
    val userService: IUserService,
    val validator: Validator,
    val inviteAddFriendService: IInviteAddFriendService
) {

    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'USER_SEARCH_BY_PHONE_NUMBER')")
    fun search(request: ServerRequest): Mono<ServerResponse?> {
        val phoneNumber = request.queryParam("phone_number").orElse("")
        return validator.validate(PhoneNumberRequest().apply { this@apply.phoneNumber = phoneNumber ?: "" }) { it ->
            val phoneNumberFormat = formatPhoneNumber(it.phoneNumber)
            ReactiveSecurityContextHolder.getContext()
                .filter { context -> context.authentication.principal != phoneNumberFormat }
                .flatMap {
                    userService.findByPhoneNumber(phoneNumberFormat)
                }
        }.flatMap { it -> ok(it) }
            .switchIfEmpty(ok(null))
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'USER_ADD_FRIEND')")
    fun addFriend(request: ServerRequest): Mono<ServerResponse?> {
        return request
            .bodyToMono(InviteAddFriendRequest::class.java)
            .flatMap { it ->
                validator.validate(it) { request ->
                    inviteAddFriendService.sendRequest(
                        request.phoneNumber,
                        request.message
                    )
                }
            }.then(ok(null))
    }
}