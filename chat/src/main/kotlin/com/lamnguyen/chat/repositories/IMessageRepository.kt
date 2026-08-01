/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 1:02 PM-26/08/2025
 *  User: kimin
 **/

package com.lamnguyen.chat.repositories

import com.lamnguyen.chat.entities.Message
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.r2dbc.repository.R2dbcRepository
import reactor.core.publisher.Flux

interface IMessageRepository : R2dbcRepository<Message, String> {
  @Query("""
        (SELECT message.id,
               message.conversation_id,
               message.sender_id,
               message.content,
               message.message_type,
               message.reply_to_id,
               message.is_pinned as is_pinned,
               message.is_deleted,
               message.created_at,
               message.updated_by,
               message.created_by,
               message.updated_by
        FROM message
        WHERE message.conversation_id in (:conversationIds)
          AND message.is_pinned = 1)
        UNION
        (SELECT message.id,
               message.conversation_id,
               message.sender_id,
               message.content,
               message.message_type,
               message.reply_to_id,
               'FALSE' as is_pinned,
               message.is_deleted,
               message.created_at,
               message.updated_by,
               message.created_by,
               message.updated_by
        FROM message
                 JOIN (SELECT message.conversation_id, MAX(id) as message_id
                       FROM message
                       WHERE message.conversation_id in (:conversationIds)
                       GROUP BY conversation_id) as mg
                      on mg.conversation_id = message.conversation_id AND mg.message_id = message.id);
  """)
    fun findAllLastMessageAndPinMessages(conversationIds: List<String>): Flux<Message>
}