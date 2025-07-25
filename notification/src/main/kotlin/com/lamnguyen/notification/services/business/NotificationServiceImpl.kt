/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 5:04 PM-24/07/2025
 *  User: kimin
 **/

package com.lamnguyen.notification.services.business

import com.google.firebase.cloud.FirestoreClient
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import com.lamnguyen.notification.messages.InviteAddFriendMessage
import com.lamnguyen.notification.utils.enums.NotificationIDEnum
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import java.time.ZoneOffset

@Service
class NotificationServiceImpl : INotificationService {
    override fun notifyInviteAddFriend(message: InviteAddFriendMessage): Mono<Void> {
        val firestore = FirestoreClient.getFirestore()
        val doc = firestore.collection("fcmTokens").document(message.phoneNumberReceiver).get().get()

        val message: Message? = Message.builder()
            .putData("id", NotificationIDEnum.ADD_FRIEND.id.toString())
            .putData("time", LocalDateTime.now().toEpochSecond(ZoneOffset.UTC).toString())
            .setNotification(
                Notification.builder()
                    .setTitle("Bạn có một lời mời kết bạn mới")
                    .setBody(message.message)
                    .setImage("https://chisworldcom.wordpress.com/wp-content/uploads/2016/12/kimi-no-nawa-2-jpg.jpg")
                    .build()
            )
            .setToken(doc.get("token").toString())
            .build()


        // Send a message to the device corresponding to the provided
        // registration token.
        val response = FirebaseMessaging.getInstance().send(message)

        // Response is a message ID string.
        println("Successfully sent message: $response")
        return Mono.empty()
    }
}