///**
// * Nguyen Dinh Lam
// * Email: kiminonawa1305@gmail.com
// * Phone number: +84 855354919
// * Create at: 12:19 PM-31/07/2025
// *  User: kimin
// **/
//
//package com.lamnguyen.user.configs.grpc
//
//import com.lamnguyen.user.configs.grpc.interceptors.AuthenticationInterceptor
//import com.lamnguyen.user.configs.grpc.interceptors.LoggingInterceptor
//import com.lamnguyen.user.utils.helpers.JwtHelper
//import com.lamnguyen.user.utils.properties.ApplicationProperty
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.grpc.server.GlobalServerInterceptor
//
//@Configuration
//class GrpcConfig {
//    @Bean
////    @GlobalServerInterceptor
//    fun initAuthenticationInterceptor(
//        authProperty: ApplicationProperty.Companion.AuthProperty,
//        jwtHelper: JwtHelper,
//    ): AuthenticationInterceptor {
//        return AuthenticationInterceptor(authProperty, jwtHelper)
//    }
//
//    @Bean
////    @GlobalServerInterceptor
//    fun initLoggingInterceptor(): LoggingInterceptor {
//        return LoggingInterceptor()
//    }
//}