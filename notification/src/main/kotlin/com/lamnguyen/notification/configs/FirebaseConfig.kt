/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 5:24 PM-13/07/2025
 *  User: kimin
 **/

package com.lamnguyen.notification.configs

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.messaging.FirebaseMessaging
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.util.ResourceUtils
import java.io.FileInputStream


@Configuration
class FirebaseConfig {
    @Bean
    fun firebaseApp(): FirebaseApp {
        val options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(FileInputStream(ResourceUtils.getFile("classpath:configs/firebase-config.json"))))
            .build()
        return FirebaseApp.initializeApp(options)
    }

    @Bean
    fun firebaseMessage(): FirebaseMessaging = FirebaseMessaging.getInstance()
}