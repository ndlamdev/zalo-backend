/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 4:47 PM-24/07/2025
 *  User: kimin
 **/

package com.lamnguyen.user.services.kafka

import com.lamnguyen.user.events.InviteAddFriendEvent
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class NotificationKafkaProducerImpl(val kafkaTemplate: KafkaTemplate<String, Any>) : INotificationKafkaProducer {
    override fun sendNotification(event: InviteAddFriendEvent) {
        kafkaTemplate.send("invite-add-friend", event)
    }
}