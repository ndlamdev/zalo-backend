/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 4:48 PM-24/07/2025
 *  User: kimin
 **/

package com.lamnguyen.user.services.kafka

import com.lamnguyen.user.events.InviteAddFriendEvent

interface INotificationKafkaProducer {
    fun sendNotification(event: InviteAddFriendEvent)
}