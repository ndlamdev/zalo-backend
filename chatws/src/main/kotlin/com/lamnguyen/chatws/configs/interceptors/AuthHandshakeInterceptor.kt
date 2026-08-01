package com.lamnguyen.chatws.configs.interceptors

import com.lamnguyen.chatws.services.redis.IRSocketMetadataCacheManager
import com.lamnguyen.chatws.utils.properties.RSocketMetadataProperty
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.server.HandshakeInterceptor

@Component
class AuthHandshakeInterceptor(
    private val rSocketMetadata: RSocketMetadataProperty,
    private val rSocketMetadataCacheManager: IRSocketMetadataCacheManager,
) : HandshakeInterceptor {
    override fun beforeHandshake(
        request: ServerHttpRequest,
        response: ServerHttpResponse,
        wsHandler: WebSocketHandler,
        attributes: MutableMap<String?, Any?>,
    ): Boolean {
        return true
    }

    override fun afterHandshake(
        request: ServerHttpRequest,
        response: ServerHttpResponse,
        wsHandler: WebSocketHandler,
        exception: Exception?,
    ) {
        val authentication = SecurityContextHolder.getContext().authentication
        if (authentication !is JwtAuthenticationToken) return
        rSocketMetadataCacheManager.cache(authentication.name, rSocketMetadata).subscribe()
    }
}
