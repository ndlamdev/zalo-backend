/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 5:08 AM-15/07/2025
 *  User: kimin
 **/

package com.lamnguyen.user.services.kafka

import com.lamnguyen.user.messages.CreateUserMessage
import com.lamnguyen.user.services.business.IUserService
import org.springframework.stereotype.Service

@Service
class UserKafkaServiceImpl(val userService: IUserService) : IUserKafkaService {
    override fun createUser(createUser: CreateUserMessage) {
        userService
            .createUser(createUser.phoneNumber)
            .subscribe { println(it) }
    }
}