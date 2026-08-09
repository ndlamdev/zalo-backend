/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 5:13 PM-28/07/2025
 *  User: kimin
 **/

package com.lamnguyen.chat.repositories

import com.lamnguyen.chat.domain.dto.MemberIdResult
import com.lamnguyen.chat.entities.Conversation
import org.springframework.data.mongodb.repository.Aggregation
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface IMemberRepository : ReactiveCrudRepository<Conversation, String> {
    @Aggregation(
        pipeline = [
            "{ \$project: {'id':  '\$members.id', phone_number:  '\$members.phone_number'  }}"
        ]
    )
    fun findAllMemberIdByConversationId(conversationId: String): Flux<MemberIdResult>

    @Aggregation(
        pipeline = [
            "{ \$match: { '_id': ?0 } }",
            "{ \$unwind: '\$members' }",
            "{ \$match: { 'members.phone_number': ?1 } }",
            "{ \$project: { 'id':  '\$members.id', phone_number:  '\$members.phone_number'} }"
        ]
    )
    fun findMemberId(conversationId: String, phoneNumber: String): Mono<MemberIdResult>
}