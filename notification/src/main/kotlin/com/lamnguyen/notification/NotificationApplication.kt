package com.lamnguyen.notification

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication
class NotificationApplication

fun main(args: Array<String>) {
    runApplication<NotificationApplication>(*args)
//
//    val registrationToken =
//        "eMwA5AEfSKaqGYqbJ8oXsw:APA91bHj4R5e6q7ihCbgTg2nVcyatDWsMmlyZZFeinckBTblkakg6qjPUk8nOK2C0KnmHEa08KF70tzKqLEaumc-1ArXVghOYpAiUVeljRUjOsosI6d-q9w"
//
////    val firestore = FirestoreClient.getFirestore()
////    val doc = firestore.collection("fcmTokens").get().get().documents
////    // See documentation on defining a message payload.
//
//
//    val message: Message? = Message.builder()
//        .putData("type", "TEXT")
//        .putData("time", LocalDateTime.now().toEpochSecond(ZoneOffset.UTC).toString()   )
//        .setNotification(
//            Notification.builder()
//                .setTitle("Nguyễn Đình Lam")
//                .setBody("Đi uống cafe nè bro!!!")
//                .setImage("https://chisworldcom.wordpress.com/wp-content/uploads/2016/12/kimi-no-nawa-2-jpg.jpg")
//                .build()
//        )
//        .setToken(registrationToken)
//        .build()
//
//
//    // Send a message to the device corresponding to the provided
//    // registration token.
//    val response = FirebaseMessaging.getInstance().send(message)
//
//    // Response is a message ID string.
//    println("Successfully sent message: $response")
}
