/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 5:13 PM-28/07/2025
 *  User: kimin
 **/

package com.lamnguyen.chat.repositories

import com.lamnguyen.chat.domain.dto.ConversationMemberRowDto
import com.lamnguyen.chat.entities.Conversation
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.r2dbc.repository.R2dbcRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface IConversationRepository : R2dbcRepository<Conversation, String> {
    @Query(
        value = """
            SELECT
                c.*,
            
                -- Metadata của người đang xem conversation
                viewer_metadata.is_pinned AS viewer_is_pinned,
            
                -- Member thuộc conversation
                member.id AS member_id,
                member.user_id AS member_user_id,
                member.role AS member_role,
                member.joined_at AS member_joined_at,
                member.joined_by AS member_joined_by,
                member.is_muted AS member_is_muted,
                member.is_active AS member_is_active,
            
                -- Metadata tương ứng của member ở trên
                member_metadata.id AS member_metadata_id,
                member_metadata.last_read_message_at AS member_last_read_message_at,
                member_metadata.is_pinned AS member_is_pinned,
                member_metadata.is_muted AS member_metadata_is_muted,
                member_metadata.is_archived AS member_is_archived
            FROM conversation c
            
            -- `viewer`: xác định các conversation mà user hiện tại được phép thấy
                     JOIN member viewer
                          ON viewer.conversation_id = c.id
                              AND viewer.user_id = :phoneNumber
                              AND viewer.is_active = true
            
                     LEFT JOIN conversation_member_metadata viewer_metadata
                               ON viewer_metadata.member_id = viewer.id
            
            -- `member`: lấy tất cả member của từng conversation đó
                     JOIN member
                          ON member.conversation_id = c.id
                              AND member.is_active = true
            
                     LEFT JOIN conversation_member_metadata member_metadata
                               ON member_metadata.member_id = member.id
            ORDER BY c.last_message_at DESC"""
    )
    fun findAllDtoByPhoneNumberContains(phoneNumber: String): Flux<ConversationMemberRowDto>

    @Query(
        value = """
            SELECT
                c.*,
            
                -- Metadata của người đang xem conversation
                viewer_metadata.is_pinned AS viewer_is_pinned,
            
                -- Member thuộc conversation
                member.id AS member_id,
                member.user_id AS member_user_id,
                member.role AS member_role,
                member.joined_at AS member_joined_at,
                member.joined_by AS member_joined_by,
                member.is_muted AS member_is_muted,
                member.is_active AS member_is_active,
            
                -- Metadata tương ứng của member ở trên
                member_metadata.id AS member_metadata_id,
                member_metadata.last_read_message_at AS member_last_read_message_at,
                member_metadata.is_pinned AS member_is_pinned,
                member_metadata.is_muted AS member_metadata_is_muted,
                member_metadata.is_archived AS member_is_archived
            FROM conversation c
            -- `viewer`: xác định các conversation mà user hiện tại được phép thấy
                     JOIN member viewer
                          ON viewer.conversation_id = c.id
                              AND viewer.user_id = :phoneNumber
                              AND viewer.is_active = true
            
                     LEFT JOIN conversation_member_metadata viewer_metadata
                               ON viewer_metadata.member_id = viewer.id
            -- `member`: lấy tất cả member của từng conversation đó
                     JOIN member
                          ON member.conversation_id = c.id
                              AND member.is_active = true
            
                     LEFT JOIN conversation_member_metadata member_metadata
                               ON member_metadata.member_id = member.id
            WHERE c.id = :conversationId
            """
    )
    fun findDtoByConversationId(phoneNumber: String, conversationId: String): Flux<ConversationMemberRowDto>

    @Query(
        value = """
            SELECT
                c.*,
            
                -- Metadata của người đang xem conversation
                viewer_metadata.is_pinned AS viewer_is_pinned,
            
                -- Member thuộc conversation
                member.id AS member_id,
                member.user_id AS member_user_id,
                member.role AS member_role,
                member.joined_at AS member_joined_at,
                member.joined_by AS member_joined_by,
                member.is_muted AS member_is_muted,
                member.is_active AS member_is_active,
            
                -- Metadata tương ứng của member ở trên
                member_metadata.id AS member_metadata_id,
                member_metadata.last_read_message_at AS member_last_read_message_at,
                member_metadata.is_pinned AS member_is_pinned,
                member_metadata.is_muted AS member_metadata_is_muted,
                member_metadata.is_archived AS member_is_archived
            FROM conversation c
            -- `viewer`: xác định các conversation mà user hiện tại được phép thấy
                     JOIN member viewer
                          ON viewer.conversation_id = c.id
                              AND viewer.user_id = :phoneNumber
                              AND viewer.is_active = true
            
                     LEFT JOIN conversation_member_metadata viewer_metadata
                               ON viewer_metadata.member_id = viewer.id
            -- `member`: lấy tất cả member của từng conversation đó
                     JOIN member
                          ON member.conversation_id = c.id
                              AND member.is_active = true
            
                     LEFT JOIN conversation_member_metadata member_metadata
                               ON member_metadata.member_id = member.id
            WHERE c.soft_id = :softId
            """
    )
    fun findDtoBySoftId(phoneNumber: String, softId: String): Flux<ConversationMemberRowDto>

    @Query(
        value = """
            SELECT
                c.*,
            
                -- Metadata của người đang xem conversation
               'FALSE' AS viewer_is_pinned,
            
                -- Member thuộc conversation
                member.id AS member_id,
                member.user_id AS member_user_id,
                member.role AS member_role,
                member.joined_at AS member_joined_at,
                member.joined_by AS member_joined_by,
                member.is_muted AS member_is_muted,
                member.is_active AS member_is_active,
            
                -- Metadata tương ứng của member ở trên
                member_metadata.id AS member_metadata_id,
                member_metadata.last_read_message_at AS member_last_read_message_at,
                member_metadata.is_pinned AS member_is_pinned,
                member_metadata.is_muted AS member_metadata_is_muted,
                member_metadata.is_archived AS member_is_archived
            FROM conversation c
            -- `member`: lấy tất cả member của từng conversation đó
                     JOIN member
                          ON member.conversation_id = c.id
                              AND member.is_active = true
            
                     LEFT JOIN conversation_member_metadata member_metadata
                               ON member_metadata.member_id = member.id
            WHERE c.id = :conversationId
            """
    )
    fun findDtoByConversationId(conversationId: String): Flux<ConversationMemberRowDto>

    @Query(
        value = """
            SELECT
                c.*,
            
                -- Metadata của người đang xem conversation
                'FALSE' AS viewer_is_pinned,
            
                -- Member thuộc conversation
                member.id AS member_id,
                member.user_id AS member_user_id,
                member.role AS member_role,
                member.joined_at AS member_joined_at,
                member.joined_by AS member_joined_by,
                member.is_muted AS member_is_muted,
                member.is_active AS member_is_active,
            
                -- Metadata tương ứng của member ở trên
                member_metadata.id AS member_metadata_id,
                member_metadata.last_read_message_at AS member_last_read_message_at,
                member_metadata.is_pinned AS member_is_pinned,
                member_metadata.is_muted AS member_metadata_is_muted,
                member_metadata.is_archived AS member_is_archived
            FROM conversation c
            -- `member`: lấy tất cả member của từng conversation đó
                     JOIN member
                          ON member.conversation_id = c.id
                              AND member.is_active = true
            
                     LEFT JOIN conversation_member_metadata member_metadata
                               ON member_metadata.member_id = member.id
            WHERE c.soft_id = :softId
            """
    )
    fun findDtoBySoftId(softId: String): Flux<ConversationMemberRowDto>

    fun findBySoftId(softId: String): Mono<Conversation>
    fun countBySoftId(softId: String): Mono<Int>
}