/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 5:13 PM-28/07/2025
 *  User: kimin
 **/

package com.lamnguyen.chat.repositories

import com.lamnguyen.chat.entities.Member
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.r2dbc.repository.R2dbcRepository
import reactor.core.publisher.Flux

interface IMemberRepository : R2dbcRepository<Member, String> {
    @Query(
        value = """
        SELECT m.*
        FROM conversation c
                 JOIN member m on m.conversation_id = c.id
        WHERE c.id = :conversationId
    """
    )
    fun findAllByConversationId(conversationId: String): Flux<Member>
}