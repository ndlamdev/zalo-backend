/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 1:44 PM-19/07/2026
 *  User: ndlamdev
 **/

package com.lamnguyen.chat.services.business.v1

import com.fasterxml.uuid.Generators
import com.lamnguyen.chat.entities.ConversationMemberMetadata
import com.lamnguyen.chat.repositories.IConversationMemberMetadataRepository
import com.lamnguyen.chat.services.business.IConversationMemberMetadataService
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class ConversationMemberMetadataServiceImpl(val cmmRepository: IConversationMemberMetadataRepository) :
    IConversationMemberMetadataService {
    override fun addMembers(
        conversationId: String,
        members: List<String>
    ): Flux<ConversationMemberMetadata> {
        val metadataList = members.stream().map { member ->
            ConversationMemberMetadata().apply {
                this.id = Generators.timeBasedEpochRandomGenerator().generate().toString()
                this.conversationId = conversationId
                this.memberId = member
                this.isNewItem = true
            }
        }.toList()

        return cmmRepository.saveAll(metadataList)
    }
}