/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 12:51 PM-26/07/2026
 *  User: ndlamdev
 **/

package com.ndlamdev.chatwsrouter.services.factory.v1

import com.ndlamdev.chatwsrouter.services.factory.IRSocketChatWsServiceRequesterFactory
import com.ndlamdev.chatwsrouter.services.redis.IRSocketMetadataChatWsServiceCacheManager
import com.ndlamdev.chatwsrouter.services.rsocket.RSocketRequesterManager
import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class RSocketChatWsServiceRequesterFactoryImpl(
    private val instanceChatWsService: IRSocketMetadataChatWsServiceCacheManager,
    private val rSocketRequesterManager: RSocketRequesterManager
) :
    IRSocketChatWsServiceRequesterFactory {
    override fun getRSocketRequester(user: String): Mono<RSocketRequester> {
        return instanceChatWsService.getRSocketMetadata(user)
            .map { rSocketRequesterManager.getOrPutChatWsServiceRequester(it) }
    }
}