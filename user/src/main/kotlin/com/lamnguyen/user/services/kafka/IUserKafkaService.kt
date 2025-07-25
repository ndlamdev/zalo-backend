/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 5:03 AM-15/07/2025
 *  User: kimin
 **/

package com.lamnguyen.user.services.kafka

import com.lamnguyen.user.messages.CreateUserMessage
import org.springframework.kafka.annotation.KafkaListener

interface IUserKafkaService {
    @KafkaListener(groupId = "user-service", topics = ["create-user"])
    fun createUser(createUser: CreateUserMessage)
}