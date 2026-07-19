/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 1:38 PM-19/07/2026
 *  User: ndlamdev
 **/

package com.lamnguyen.chat.repositories

import com.lamnguyen.chat.entities.ConversationMemberMetadata
import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.stereotype.Repository

@Repository
interface IConversationMemberMetadataRepository : R2dbcRepository<ConversationMemberMetadata, String> {

}