/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 6:10 PM-30/07/2025
 *  User: kimin
 **/

package com.lamnguyen.user.configs.grpc.interceptors

import com.lamnguyen.user.services.grpc.UserGrpcServiceImpl
import com.lamnguyen.user.utils.annotation.GrpcPreAuthorizeHasAnyAuthority
import io.grpc.*
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class AuthorizationInterceptor : ServerInterceptor {
    override fun <ReqT : Any?, RespT : Any?> interceptCall(
        serverCall: ServerCall<ReqT, RespT>,
        metaData: Metadata,
        serverCallHandler: ServerCallHandler<ReqT, RespT>
    ): ServerCall.Listener<ReqT?>? {
        val annotation = serverCall.methodDescriptor.bareMethodName?.let {
            val clazz = UserGrpcServiceImpl::class.java
            clazz.methods.find { method ->
                method.name.equals(it, true)
            }?.getAnnotation(GrpcPreAuthorizeHasAnyAuthority::class.java)
        }

        if (annotation == null)
            return serverCallHandler.startCall(serverCall, metaData)

        val authentication = SecurityContextHolder.getContext().authentication
        if (authentication == null) {
            serverCall.close(
                Status
                    .UNAUTHENTICATED
                    .withDescription("Authentication failed"),
                Metadata()
            )
            return object : ServerCall.Listener<ReqT?>() {}
        }

        for (authority in annotation.value) {
            if (authentication.authorities.contains(SimpleGrantedAuthority(authority)))
                return serverCallHandler.startCall(serverCall, metaData)
        }

        serverCall.close(
            Status
                .PERMISSION_DENIED
                .withDescription("Permission denied"),
            Metadata()
        )
        return object : ServerCall.Listener<ReqT?>() {}
    }
}