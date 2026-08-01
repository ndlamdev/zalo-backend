/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 1:31 PM-26/07/2026
 *  User: ndlamdev
 **/

package com.lamnguyen.chatws.configs

import com.lamnguyen.chatws.services.redis.IRSocketMetadataCacheManager
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.stereotype.Component
import org.springframework.web.socket.messaging.SessionDisconnectEvent


@Component
class WebSocketDisconnectListener(private val rSocketMetadataCacheManager: IRSocketMetadataCacheManager) {
    private val log = LoggerFactory.getLogger(WebSocketDisconnectListener::class.java)

    @EventListener
    fun handleWebSocketDisconnectListener(event: SessionDisconnectEvent) {
        val headerAccessor = StompHeaderAccessor.wrap(event.message)

        val sessionId = event.sessionId
        val username = if (headerAccessor.user != null) headerAccessor.user!!.name else "Anonymous"

        log.info("User disconnected: $username (Session ID: $sessionId)")

        rSocketMetadataCacheManager.clear(username)
    }
}