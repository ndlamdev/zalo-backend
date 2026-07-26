package com.lamnguyen.chatws.configs.handlers

import org.springframework.http.server.ServerHttpRequest
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.server.support.DefaultHandshakeHandler
import java.security.Principal

@Component
class UserHandshakeHandler : DefaultHandshakeHandler() {
    override fun determineUser(
        request: ServerHttpRequest,
        wsHandler: WebSocketHandler,
        attributes: MutableMap<String?, Any?>,
    ): Principal? {
        try {
            val phoneNumber = attributes["phone_number"] as String
            val token = attributes["token"] as String
            return StompPrincipal(phoneNumber, token)
        } catch (_: Exception) {
            return null
        }
    }

    class StompPrincipal(private val name: String?, var token: String) : Principal {
        override fun getName(): String? {
            return name
        }
    }

    
}