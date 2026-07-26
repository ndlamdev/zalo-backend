/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 12:36 PM-26/07/2026
 *  User: ndlamdev
 **/

package com.ndlamdev.chatwsrouter.services.redis

import com.ndlamdev.chatwsrouter.domain.dto.RSocketMetadata
import com.ndlamdev.chatwsrouter.utils.redis.ICacheRedis
import reactor.core.publisher.Mono

interface IRSocketMetadataChatWsServiceCacheManager : ICacheRedis<RSocketMetadata> {
    fun getRSocketMetadata(user: String): Mono<RSocketMetadata>
}