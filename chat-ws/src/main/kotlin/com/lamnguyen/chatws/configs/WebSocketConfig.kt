/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 3:58 AM-05/07/2025
 *  User: kimin
 **/

package com.lamnguyen.chatws.configs

import com.lamnguyen.chatws.configs.handlers.UserHandshakeHandler
import com.lamnguyen.chatws.configs.interceptors.AuthHandshakeInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer


@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig(
    val authHandshakeInterceptor: AuthHandshakeInterceptor,
    val userHandshakeHandler: UserHandshakeHandler,
) : WebSocketMessageBrokerConfigurer {
    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        registry.enableSimpleBroker("/notify", "/queue") // prefix nhận subscribe 1 topic
        registry.setApplicationDestinationPrefixes("/app") // prefix nhận message và gửi cho toàn app (Không đăng nhập)
        registry.setUserDestinationPrefix("/user") //  prefix nhận message và gửi cho từng user (Đã đăng nhập)
    }

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint("/chat-websocket")
            .addInterceptors(authHandshakeInterceptor)
            .setHandshakeHandler(userHandshakeHandler)
            .setAllowedOrigins("*") // đăng ký connect
    }
}