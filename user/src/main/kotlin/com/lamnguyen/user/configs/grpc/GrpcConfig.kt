/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 12:19 PM-31/07/2025
 *  User: kimin
 **/

package com.lamnguyen.user.configs.grpc

import com.lamnguyen.user.configs.grpc.registor.GrpcAuthorizationInterceptorManager
import com.lamnguyen.user.services.grpc.UserGrpcServiceImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GrpcConfig {
    @Bean
    fun authenticationRegister(): GrpcAuthorizationInterceptorManager {
        return GrpcAuthorizationInterceptorManager(UserGrpcServiceImpl::class.java)
    }
}