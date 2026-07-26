package com.lamnguyen.chatws.configs.interceptors

import com.lamnguyen.chatws.services.redis.IRSocketMetadataCacheManager
import com.lamnguyen.chatws.utils.helpers.JwtHelper
import com.lamnguyen.chatws.utils.properties.RSocketMetadataProperty
import org.springframework.http.HttpHeaders
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.http.server.ServletServerHttpRequest
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.server.HandshakeInterceptor

@Component
class AuthHandshakeInterceptor(
    private val jwtHelper: JwtHelper,
    private val rSocketMetadata: RSocketMetadataProperty,
    private val rSocketMetadataCacheManager: IRSocketMetadataCacheManager
) : HandshakeInterceptor {
    var user: String? = null

    override fun beforeHandshake(
        request: ServerHttpRequest,
        response: ServerHttpResponse,
        wsHandler: WebSocketHandler,
        attributes: MutableMap<String?, Any?>,
    ): Boolean {
        try {
            val token = (request as ServletServerHttpRequest)
                .servletRequest
                .getHeader(HttpHeaders.AUTHORIZATION).substring(7)
            val authToken = jwtHelper.initAuthenticationToken(token, mutableSetOf())
            attributes["phone_number"] = authToken.name
            user = authToken.name
            attributes["token"] = token
            return true
        } catch (_: Exception) {
            return false
        }
    }

    override fun afterHandshake(
        request: ServerHttpRequest,
        response: ServerHttpResponse,
        wsHandler: WebSocketHandler,
        exception: Exception?,
    ) {
        if (user.isNullOrBlank()) return
        rSocketMetadataCacheManager.cache(user!!, rSocketMetadata).subscribe()
    }
}