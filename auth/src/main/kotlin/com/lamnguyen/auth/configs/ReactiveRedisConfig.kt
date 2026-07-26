package com.lamnguyen.auth.configs

import com.lamnguyen.auth.model.Permission
import com.lamnguyen.auth.model.Role
import com.lamnguyen.auth.utils.redis.serializers.KryoRedisSerializer
import org.redisson.spring.data.connection.RedissonConnectionFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
class ReactiveRedisConfig {
    @Bean
    fun reactiveRedisTemplate(factory: RedissonConnectionFactory): ReactiveRedisTemplate<String, Any> {
        val keySerializer = StringRedisSerializer()
        val valueSerializer = GenericJackson2JsonRedisSerializer()

        val serializationContext = RedisSerializationContext
            .newSerializationContext<String, Any>(keySerializer)
            .value(valueSerializer)
            .build()

        return ReactiveRedisTemplate(factory, serializationContext)
    }


    @Bean
    fun roleRedissonClient(factory: RedissonConnectionFactory): ReactiveRedisTemplate<String, Role?> {
        val context = RedisSerializationContext
            .newSerializationContext<String, Role?>(StringRedisSerializer())
            .value(KryoRedisSerializer(Role::class.java))
            .build()

        return ReactiveRedisTemplate(factory, context)
    }

    @Bean
    fun permissionRedissonClient(factory: RedissonConnectionFactory): ReactiveRedisTemplate<String, Permission?> {
        val context = RedisSerializationContext
            .newSerializationContext<String, Permission?>(StringRedisSerializer())
            .value(KryoRedisSerializer(Permission::class.java))
            .build()

        return ReactiveRedisTemplate(factory, context)
    }
}