package com.lamnguyen.chatws.configs

import com.lamnguyen.chatws.utils.properties.RSocketMetadataProperty
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
    fun rSocketMetadataPropertyReactiveRedisTemplate(factory: RedissonConnectionFactory): ReactiveRedisTemplate<String, RSocketMetadataProperty> {
        val keySerializer = StringRedisSerializer()
        val valueSerializer = GenericJackson2JsonRedisSerializer() as RedisSerializer<RSocketMetadataProperty>

        val serializationContext = RedisSerializationContext
            .newSerializationContext<String, RSocketMetadataProperty>(keySerializer)
            .value(valueSerializer)
            .build()

        return ReactiveRedisTemplate(factory, serializationContext)
    }
}