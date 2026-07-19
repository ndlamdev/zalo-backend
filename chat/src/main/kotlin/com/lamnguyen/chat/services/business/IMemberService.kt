/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 5:16 PM-28/07/2025
 *  User: kimin
 **/

package com.lamnguyen.chat.services.business

import com.lamnguyen.chat.entities.Member
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface IMemberService {
    fun addMembersRoleUser(
        conversationId: String,
        joinBy: String,
        userIds: List<String>,
    ): Flux<Member>

    fun addMemberRoleAdmin(
        conversationId: String,
        memberId: String,
        userId: String,
    ): Mono<Member>

    fun getMembersInConversation(conversationId: String): Flux<Member>
}