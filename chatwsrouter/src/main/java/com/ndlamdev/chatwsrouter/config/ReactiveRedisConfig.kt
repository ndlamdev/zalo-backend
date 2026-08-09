package com.ndlamdev.chatwsrouter.config

import com.ndlamdev.chatwsrouter.domain.dto.ConversationDto
import com.ndlamdev.chatwsrouter.domain.dto.ConversationDto.Companion.ConversationType
import com.ndlamdev.chatwsrouter.domain.dto.MemberDto
import com.ndlamdev.chatwsrouter.domain.dto.MemberDto.Companion.MemberRole
import com.ndlamdev.chatwsrouter.domain.dto.RSocketMetadata
import com.ndlamdev.chatwsrouter.utils.redis.serializers.KryoRedisSerializer
import org.redisson.spring.data.connection.RedissonConnectionFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.time.LocalDateTime

@Configuration
class ReactiveRedisConfig {
    @Bean
    fun rSocketMetadataPropertyReactiveRedisTemplate(
        factory: RedissonConnectionFactory,
    ): ReactiveRedisTemplate<String, RSocketMetadata> {
        val keySerializer = StringRedisSerializer()
        val valueSerializer = KryoRedisSerializer(RSocketMetadata::class.java)

        val serializationContext = RedisSerializationContext
            .newSerializationContext<String, RSocketMetadata>(keySerializer)
            .value(valueSerializer)
            .hashValue(valueSerializer)
            .build()

        return ReactiveRedisTemplate(factory, serializationContext)
    }

    @Bean
    fun conversationDtoReactiveRedisTemplate(
        factory: RedissonConnectionFactory,
    ): ReactiveRedisTemplate<String, ConversationDto> {
        val keySerializer = StringRedisSerializer()
        val valueSerializer =
            KryoRedisSerializer(
                ConversationDto::class.java,
                LocalDateTime::class.java,
                ConversationType::class.java,
                MemberDto::class.java,
                MemberRole::class.java,
                ArrayList::class.java
            )

        val serializationContext = RedisSerializationContext
            .newSerializationContext<String, ConversationDto>(keySerializer)
            .value(valueSerializer)
            .hashValue(valueSerializer)
            .build()

        return ReactiveRedisTemplate(factory, serializationContext)
    }
}