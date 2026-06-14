/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 4:37 PM-14/07/2025
 *  User: kimin
 **/

package com.lamnguyen.auth.service.kafka

import com.lamnguyen.auth.events.CreateUserEvent
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class UserKafkaServiceImpl(val kafkaTemplate: KafkaTemplate<String, Any>) : IUserKafkaService {
    override fun createUser(phoneNumber: String): Mono<Boolean> {
        kafkaTemplate.send("create-user", CreateUserEvent(phoneNumber)).isDone
        return Mono.just(true)
    }
}