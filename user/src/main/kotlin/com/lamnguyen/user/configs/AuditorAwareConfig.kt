package com.lamnguyen.user.configs

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.ReactiveAuditorAware
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import reactor.core.publisher.Mono

@Configuration
@EnableR2dbcAuditing
class AuditorAwareConfig {

    @Bean
    fun myAuditorProvider(): ReactiveAuditorAware<String> {
        return ReactiveAuditorAware<String> {
            ReactiveSecurityContextHolder.getContext().flatMap { securityContext ->
                val auth = securityContext.authentication ?: return@flatMap Mono.empty()
                return@flatMap Mono.just(auth.name)
            }
        }
    }
}