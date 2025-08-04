package com.lamnguyen.auth.configs

import org.redisson.Redisson
import org.redisson.api.RedissonReactiveClient
import org.redisson.config.Config
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.Resource

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

//    @Bean
//    fun redissonConnectionFactory(config: Config): RedissonConnectionFactory? {
//        return RedissonConnectionFactory(config)
//    }

//    override fun getConfigClasses(): Array<out Class<*>?> {
//        return arrayOf()
//    }
}