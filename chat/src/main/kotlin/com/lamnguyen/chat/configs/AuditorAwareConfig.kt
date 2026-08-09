package com.lamnguyen.chat.configs

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.ReactiveAuditorAware
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing
import org.springframework.security.core.context.SecurityContextHolder
import reactor.core.publisher.Mono

@Configuration
@EnableReactiveMongoAuditing
class AuditorAwareConfig {

    @Bean
    fun initAuditorProvider(): ReactiveAuditorAware<String> {
        return ReactiveAuditorAware<String> {
            val auth = SecurityContextHolder.getContext().authentication ?: return@ReactiveAuditorAware Mono.empty()
            return@ReactiveAuditorAware Mono.just(auth.name)
        }
    }
}