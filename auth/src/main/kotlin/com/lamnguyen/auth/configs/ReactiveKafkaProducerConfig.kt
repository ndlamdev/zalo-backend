/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 11:27 AM-26/07/2026
 *  User: ndlamdev
 **/

package com.lamnguyen.auth.configs

import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate
import reactor.kafka.sender.SenderOptions


@Configuration
class ReactiveKafkaProducerConfig {
    @Bean
    fun reactiveKafkaProducerTemplate(
        properties: KafkaProperties
    ): ReactiveKafkaProducerTemplate<String, Any> {
        val props = properties.buildProducerProperties()
        return ReactiveKafkaProducerTemplate(SenderOptions.create(props))
    }
}