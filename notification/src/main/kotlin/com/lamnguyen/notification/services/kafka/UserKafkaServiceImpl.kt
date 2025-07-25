/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 5:08 AM-15/07/2025
 *  User: kimin
 **/

package com.lamnguyen.notification.services.kafka

import com.lamnguyen.notification.messages.CreateUserMessage
import com.lamnguyen.notification.messages.InviteAddFriendMessage
import com.lamnguyen.notification.services.business.INotificationService
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class UserKafkaServiceImpl(val notificationService: INotificationService) : IUserKafkaService {
    override fun sendOtp(message: CreateUserMessage) {
    }

    override fun inviteAddFriend(message: InviteAddFriendMessage) {
        notificationService
            .notifyInviteAddFriend(message)
            .subscribe()
    }
}