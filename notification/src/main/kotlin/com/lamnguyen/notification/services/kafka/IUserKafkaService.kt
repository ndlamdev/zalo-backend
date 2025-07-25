/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 5:03 AM-15/07/2025
 *  User: kimin
 **/

package com.lamnguyen.notification.services.kafka

import com.lamnguyen.notification.messages.CreateUserMessage
import com.lamnguyen.notification.messages.InviteAddFriendMessage
import org.springframework.kafka.annotation.KafkaListener
import reactor.core.publisher.Mono

interface IUserKafkaService {
    @KafkaListener(groupId = "notification-service", topics = ["create-user"])
    fun sendOtp(message: CreateUserMessage)

    @KafkaListener(groupId = "notification-service", topics = ["invite-add-friend"])
    fun inviteAddFriend(message: InviteAddFriendMessage)
}