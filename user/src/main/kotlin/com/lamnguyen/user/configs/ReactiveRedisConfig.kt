package com.lamnguyen.user.configs

import com.lamnguyen.user.models.User
import com.lamnguyen.user.utils.redis.serializers.KryoRedisSerializer
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
    fun conversationDtoReactiveRedisTemplate(
        factory: RedissonConnectionFactory,
    ): ReactiveRedisTemplate<String, User> {
        val keySerializer = StringRedisSerializer()
        val valueSerializer =
            KryoRedisSerializer(
                User::class.java,
                LocalDateTime::class.java,
            )

        val serializationContext = RedisSerializationContext
            .newSerializationContext<String, User>(keySerializer)
            .value(valueSerializer)
            .hashValue(valueSerializer)
            .build()

        return ReactiveRedisTemplate(factory, serializationContext)
    }
}