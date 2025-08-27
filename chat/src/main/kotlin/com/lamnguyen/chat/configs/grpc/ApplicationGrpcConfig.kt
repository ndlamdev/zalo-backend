/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 12:19 PM-31/07/2025
 *  User: kimin
 **/

package com.lamnguyen.chat.configs.grpc

import com.lamnguyen.chat.configs.grpc.registor.GrpcAuthorizationInterceptorManager
import com.lamnguyen.chat.protos.UserServiceGrpc
import com.lamnguyen.chat.services.grpc.v1.UserGrpcServiceImpl
import net.devh.boot.grpc.client.inject.GrpcClient
import net.devh.boot.grpc.client.inject.GrpcClientBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@GrpcClientBean(
    clazz = UserServiceGrpc.UserServiceBlockingStub::class,
    beanName = "userBlockingStub",
    client = GrpcClient("user")
)
class ApplicationGrpcConfig {
    @Bean
    fun authenticationRegister(): GrpcAuthorizationInterceptorManager {
        return GrpcAuthorizationInterceptorManager(UserGrpcServiceImpl::class.java)
    }
}