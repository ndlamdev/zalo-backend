/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 4:10 PM-30/07/2025
 *  User: kimin
 **/

package com.lamnguyen.user.configs.grpc.interceptors

import io.grpc.Metadata
import io.grpc.ServerCall
import io.grpc.ServerCallHandler
import io.grpc.ServerInterceptor
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.Ordered
import org.springframework.grpc.server.GlobalServerInterceptor
import org.springframework.stereotype.Component

@Component
@GlobalServerInterceptor
class LoggingInterceptor : ServerInterceptor, Ordered {
    override fun <ReqT : Any?, RespT : Any?> interceptCall(
        serverCall: ServerCall<ReqT, RespT>,
        metaData: Metadata,
        serverCallHandler: ServerCallHandler<ReqT, RespT>
    ): ServerCall.Listener<ReqT?>? {
        LOGGER.info("ServerCall: ${serverCall.methodDescriptor}")
        LOGGER.info("Metadata: $metaData")
        return serverCallHandler.startCall(serverCall, metaData)
    }

    override fun getOrder(): Int = -100

    companion object {
        val LOGGER: Logger = LoggerFactory.getLogger(LoggingInterceptor::class.java)
    }
}