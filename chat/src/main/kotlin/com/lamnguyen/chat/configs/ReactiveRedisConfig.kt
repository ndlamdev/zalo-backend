package com.lamnguyen.chat.configs

import com.lamnguyen.chat.domain.dto.ConversationDto
import com.lamnguyen.chat.entities.Conversation
import com.lamnguyen.chat.entities.ConversationMemberMetadata
import com.lamnguyen.chat.utils.redis.serializers.KryoRedisSerializer
import org.redisson.spring.data.connection.RedissonConnectionFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
class ReactiveRedisConfig {

    @Bean
    fun reactiveRedisTemplate(factory: ReactiveRedisConnectionFactory): ReactiveRedisTemplate<String, Any> {
        val keySerializer = StringRedisSerializer()
        val valueSerializer = GenericJackson2JsonRedisSerializer()

        val serializationContext = RedisSerializationContext
            .newSerializationContext<String, Any>(keySerializer)
            .value(valueSerializer)
            .build()

        return ReactiveRedisTemplate(factory, serializationContext)
    }

    @Bean
    fun conversationRedissonClient(factory: RedissonConnectionFactory): ReactiveRedisTemplate<String?, ConversationDto?> {
        val keySerializer = StringRedisSerializer()
        val valueSerializer = KryoRedisSerializer(ConversationDto::class.java)

        val context = RedisSerializationContext
            .newSerializationContext<String?, ConversationDto?>(keySerializer)
            .value(valueSerializer)
            .build()

        return ReactiveRedisTemplate(factory, context)
    }
}