package com.lamnguyen.chat.configs.grpc

import com.lamnguyen.chat.protos.UserServiceGrpc
import net.devh.boot.grpc.client.inject.GrpcClient
import net.devh.boot.grpc.client.inject.GrpcClientBean
import org.springframework.context.annotation.Configuration


/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 2:12 PM-30/07/2025
 *  User: kimin
 **/
@Configuration
@GrpcClientBean(
    clazz = UserServiceGrpc.UserServiceBlockingStub::class,
    beanName = "userBlockingStub",
    client = GrpcClient("user")
)
class GrpcConfig {
}