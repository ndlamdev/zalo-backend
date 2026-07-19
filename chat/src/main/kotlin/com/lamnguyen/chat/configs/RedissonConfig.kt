package com.lamnguyen.chat.configs

import com.lamnguyen.chat.entities.Conversation
import com.lamnguyen.chat.entities.ConversationMemberMetadata
import com.lamnguyen.chat.utils.redis.serializers.KryoRedisSerializer
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
    fun conversationRedissonClient(factory: RedissonConnectionFactory): ReactiveRedisTemplate<String?, Conversation?> {
        val context = RedisSerializationContext
            .newSerializationContext<String?, Conversation?>(StringRedisSerializer())
            .value(KryoRedisSerializer(Conversation::class.java))
            .build()

        return ReactiveRedisTemplate(factory, context)
    }

    @Bean
    fun conversationMemberRedissonClient(factory: RedissonConnectionFactory): ReactiveRedisTemplate<String?, ConversationMemberMetadata?> {
        val context = RedisSerializationContext
            .newSerializationContext<String?, ConversationMemberMetadata?>(StringRedisSerializer())
            .value(KryoRedisSerializer(ConversationMemberMetadata::class.java))
            .build()

        return ReactiveRedisTemplate(factory, context)
    }
}