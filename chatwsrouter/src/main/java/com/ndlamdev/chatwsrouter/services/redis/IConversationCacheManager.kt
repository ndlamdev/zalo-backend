/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 12:12 PM-26/07/2026
 *  User: ndlamdev
 **/

package com.ndlamdev.chatwsrouter.services.redis

import com.ndlamdev.chatwsrouter.domain.dto.ConversationDto
import com.ndlamdev.chatwsrouter.utils.redis.ICacheRedis
import reactor.core.publisher.Mono

interface IConversationCacheManager : ICacheRedis<ConversationDto> {
    fun getConversationInfoByConversationId(conversationId: String): Mono<ConversationDto>
    fun getConversationInfoBySoftId(softId: String): Mono<ConversationDto>
    fun cacheConversation(conversation: Mono<ConversationDto>): Mono<ConversationDto>
}