/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 6:47 PM-13/06/2026
 *  User: ndlamdev
 **/

package com.lamnguyen.auth.service.kafka

import com.lamnguyen.auth.events.OtpCreateAccountEvent
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class SmsSenderKafkaServiceImpl(val kafkaTemplate: ReactiveKafkaProducerTemplate<String, Any>) :
    IOtpSenderKafkaService {
    override fun sendOtp(address: String, otp: String): Mono<Void> {
        return kafkaTemplate.send("create-account-otp", OtpCreateAccountEvent(address, otp)).then()
    }
}