/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 12:51 PM-24/07/2025
 *  User: kimin
 **/

package com.lamnguyen.user.services.business

import com.lamnguyen.user.models.InviteAddFriend
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface IInviteAddFriendService {
    fun sendRequest(phoneNumberReceiver: String, message: String?): Mono<Void>
    fun findInviteAddFriend(phoneNumberSender: String, phoneNumberReceiver: String): Mono<InviteAddFriend>
    fun replyInvite(id: Long, answer: Boolean): Mono<Void>
    fun getAllInvite(phoneNumberReceiver: String): Flux<InviteAddFriend>
    fun getAllRequest(phoneNumberSender: String): Flux<InviteAddFriend>
}