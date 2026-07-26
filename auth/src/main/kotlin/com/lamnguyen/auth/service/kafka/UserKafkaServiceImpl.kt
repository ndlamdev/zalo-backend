/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 4:37 PM-14/07/2025
 *  User: kimin
 **/

package com.lamnguyen.auth.service.kafka

import com.lamnguyen.auth.events.CreateUserEvent
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class UserKafkaServiceImpl(private val kafkaTemplate: ReactiveKafkaProducerTemplate<String, Any>) : IUserKafkaService {
    override fun createUser(phoneNumber: String): Mono<Boolean> {
        return kafkaTemplate.send("create-user", CreateUserEvent(phoneNumber))
            .thenReturn(true)
    }
}