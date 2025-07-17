/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 5:08 AM-15/07/2025
 *  User: kimin
 **/

package com.lamnguyen.notification.services.kafka

import com.lamnguyen.notification.messages.CreateUser
import com.lamnguyen.notification.services.business.IUserService
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class UserKafkaServiceImpl(val userService: IUserService) : IUserKafkaService {
    override fun sendOtp(createUser: CreateUser): Mono<Void> {
        return Mono.empty()
    }
}