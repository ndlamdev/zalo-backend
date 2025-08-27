package com.lamnguyen.chatws.configs.interceptors

import com.lamnguyen.chatws.utils.helpers.JwtHelper
import org.springframework.http.HttpHeaders
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.http.server.ServletServerHttpRequest
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.server.HandshakeInterceptor

@Component
class AuthHandshakeInterceptor(private val jwtHelper: JwtHelper) : HandshakeInterceptor {
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
            attributes.put("phone_number", authToken.name)
            attributes.put("token", token)
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
    }
}