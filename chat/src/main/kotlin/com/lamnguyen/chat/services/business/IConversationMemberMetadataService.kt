/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 1:43 PM-19/07/2026
 *  User: ndlamdev
 **/

package com.lamnguyen.chat.services.business

import com.lamnguyen.chat.entities.ConversationMemberMetadata
import reactor.core.publisher.Flux

interface IConversationMemberMetadataService {
    fun addMembers(conversationId: String, members: List<String>): Flux<ConversationMemberMetadata>
}