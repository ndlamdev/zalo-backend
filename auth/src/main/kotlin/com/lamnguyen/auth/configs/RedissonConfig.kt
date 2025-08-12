package com.lamnguyen.auth.configs

import com.lamnguyen.auth.model.Permission
import com.lamnguyen.auth.model.Role
import com.lamnguyen.auth.utils.KryoRedisSerializer
import org.redisson.Redisson
import org.redisson.api.RedissonReactiveClient
import org.redisson.config.Config
import org.redisson.spring.data.connection.RedissonConnectionFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.Resource
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.time.LocalDateTime


@Configuration("project-redisson-config")
class RedissonConfig {
    @Bean
    fun redissonConfig(@Value("classpath:/redisson.yaml") configFile: Resource): Config {
        return Config.fromYAML(configFile.inputStream)
    }

    @Bean
    fun redisson(config: Config): RedissonReactiveClient {
        return Redisson.create(config).reactive()
    }

    @Bean
    fun redissonConnectionFactory(config: Config): RedissonConnectionFactory {
        return RedissonConnectionFactory(config)
    }

    @Bean
    fun roleRedissonClient(factory: RedissonConnectionFactory): ReactiveRedisTemplate<String, Role?> {
        val context = RedisSerializationContext
            .newSerializationContext<String?, Role?>(StringRedisSerializer())
            .value(KryoRedisSerializer(Role::class.java, LocalDateTime::class.java))
            .build()

        return ReactiveRedisTemplate(factory, context)
    }

    @Bean
    fun permissionRedissonClient(factory: RedissonConnectionFactory): ReactiveRedisTemplate<String, Permission?> {
        val context = RedisSerializationContext
            .newSerializationContext<String?, Permission?>(StringRedisSerializer())
            .value(KryoRedisSerializer(Permission::class.java, LocalDateTime::class.java))
            .build()

        return ReactiveRedisTemplate(factory, context)
    }


}