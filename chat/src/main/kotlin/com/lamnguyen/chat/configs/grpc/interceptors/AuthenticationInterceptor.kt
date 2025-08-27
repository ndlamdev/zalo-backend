/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 6:10 PM-30/07/2025
 *  User: kimin
 **/

package com.lamnguyen.chat.configs.grpc.interceptors

import com.lamnguyen.chat.utils.helpers.JwtHelper
import com.lamnguyen.chat.utils.properties.ApplicationProperty
import io.grpc.*
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor
import org.springframework.core.Ordered
import org.springframework.http.HttpHeaders
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
@GrpcGlobalServerInterceptor
class AuthenticationInterceptor(
    val authProperty: ApplicationProperty.Companion.AuthProperty,
    val jwtHelper: JwtHelper,
    val interceptor: AuthorizationInterceptor,
) : ServerInterceptor, Ordered {

    override fun <ReqT : Any?, RespT : Any?> interceptCall(
        serverCall: ServerCall<ReqT, RespT>,
        metaData: Metadata,
        serverCallHandler: ServerCallHandler<ReqT, RespT>,
    ): ServerCall.Listener<ReqT?>? {
        try {
            SecurityContextHolder.getContext().authentication = extractToken(metaData)
            return interceptor.interceptCall(serverCall, metaData, serverCallHandler)
        } catch (ex: Exception) {
            val status = Status.UNAUTHENTICATED
                .withDescription("Token error: ${ex.message}")
                .withCause(ex)
            serverCall.close(status, Metadata())
            return object : ServerCall.Listener<ReqT?>() {}
        }
    }

    override fun getOrder(): Int = -10

    private fun extractToken(metaData: Metadata): Authentication? {
        val bearerToken = metaData.get(
            Metadata.Key.of(
                HttpHeaders.AUTHORIZATION,
                Metadata.ASCII_STRING_MARSHALLER
            )
        )
        if (bearerToken.isNullOrEmpty())
            return null

        val authorities =
            metaData.getAll(Metadata.Key.of(authProperty.userRoles, Metadata.ASCII_STRING_MARSHALLER))
                ?.map { SimpleGrantedAuthority(it) }
                ?.toMutableSet()
                ?: mutableSetOf()

        val token = bearerToken.substring(7)

        return try {
            jwtHelper.initJwtAuthenticationToken(token, authorities)
        } catch (_: Exception) {
            null
        }
    }
}