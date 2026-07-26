/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 12:10 PM-26/07/2026
 *  User: ndlamdev
 **/

package com.ndlamdev.chatwsrouter.services.business.v1

import com.ndlamdev.chatwsrouter.domain.dto.ConversationDto
import com.ndlamdev.chatwsrouter.services.business.IConversationService
import com.ndlamdev.chatwsrouter.services.redis.IConversationCacheManager
import com.ndlamdev.chatwsrouter.services.rsocket.IChatServiceRequester
import com.ndlamdev.chatwsrouter.utils.helper.KeyGenerator
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class ConversationServiceImpl(
    private val chatServiceRequester: IChatServiceRequester,
    private val conversationCacheManager: IConversationCacheManager
) : IConversationService {
    override fun getConversationInfo(
        owner: String,
        users: List<String>
    ): Mono<ConversationDto> {
        val distinctUser = users.distinct().toMutableList()
        if (!distinctUser.contains(owner)) distinctUser.add(owner)

        val softId =
            KeyGenerator.generateSoftIdConversation(distinctUser)
                ?: throw Exception("Generate soft id of conversation was failed")

        return conversationCacheManager.getConversationInfoBySoftId(softId)
            .switchIfEmpty(
                conversationCacheManager.cacheConversation(chatServiceRequester.getConversationInfo(owner, users))
            )
    }

    override fun getConversationInfo(conversationId: String): Mono<ConversationDto> {
        return conversationCacheManager.getConversationInfoByConversationId(conversationId)
            .switchIfEmpty(
                conversationCacheManager.cacheConversation(chatServiceRequester.getConversationInfo(conversationId))
            )
    }
}