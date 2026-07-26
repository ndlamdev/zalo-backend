package com.ndlamdev.chatwsrouter.config

import com.ndlamdev.chatwsrouter.domain.dto.ConversationDto
import com.ndlamdev.chatwsrouter.domain.dto.RSocketMetadata
import org.redisson.spring.data.connection.RedissonConnectionFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.RedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
class ReactiveRedisConfig {
    @Bean
    fun rSocketMetadataPropertyReactiveRedisTemplate(factory: RedissonConnectionFactory): ReactiveRedisTemplate<String, RSocketMetadata> {
        val keySerializer = StringRedisSerializer()
        val valueSerializer = GenericJackson2JsonRedisSerializer() as RedisSerializer<RSocketMetadata>

        val serializationContext = RedisSerializationContext
            .newSerializationContext<String, RSocketMetadata>(keySerializer)
            .value(valueSerializer)
            .build()

        return ReactiveRedisTemplate(factory, serializationContext)
    }

    @Bean
    fun conversationDtoReactiveRedisTemplate(factory: RedissonConnectionFactory): ReactiveRedisTemplate<String, ConversationDto> {
        val keySerializer = StringRedisSerializer()
        val valueSerializer = GenericJackson2JsonRedisSerializer() as RedisSerializer<ConversationDto>

        val serializationContext = RedisSerializationContext
            .newSerializationContext<String, ConversationDto>(keySerializer)
            .value(valueSerializer)
            .build()

        return ReactiveRedisTemplate(factory, serializationContext)
    }
}