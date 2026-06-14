/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 7:32 PM-11/04/2026
 *  User: kimin
 **/

package com.lamnguyen.auth.handlers

import com.lamnguyen.auth.domain.requests.ConfirmQrLogin
import com.lamnguyen.auth.domain.requests.TokenRequest
import com.lamnguyen.auth.exceptions.ApplicationException
import com.lamnguyen.auth.exceptions.ExceptionEnum
import com.lamnguyen.auth.service.business.IQrService
import com.lamnguyen.auth.utils.helpers.ok
import com.lamnguyen.auth.utils.helpers.okTextEventStream
import com.lamnguyen.auth.utils.properties.ApplicationProperty
import org.springframework.http.ResponseCookie
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyToMono
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import kotlin.jvm.optionals.getOrElse

@Component
class QrHandler(
    val qrService: IQrService,
    val refreshTokenProperty: ApplicationProperty.Companion.AuthProperty.Companion.JwtProperty.Companion.RefreshTokenProperty,
) {
    fun generateQr(request: ServerRequest): Mono<ServerResponse?> {
        return qrService.createQrCodeLoginAndToken()
            .flatMap { ok(it, "Generate qr-code success") }
    }

    fun subscribe(request: ServerRequest): Mono<ServerResponse?> {
        val qrToken = request.queryParam("token").getOrElse { "" }
        return okTextEventStream(qrService.subscribe(qrToken))
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    fun scan(request: ServerRequest): Mono<ServerResponse?> {
        return request.bodyToMono<TokenRequest>()
            .switchIfEmpty { Mono.error { ApplicationException(ExceptionEnum.EMPTY_DATA) } }
            .flatMap { qrService.scan(it.token) }
            .then(ok("Scan success,please confirm action!"))
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    fun confirm(request: ServerRequest): Mono<ServerResponse?> {
        return request.bodyToMono<ConfirmQrLogin>()
            .switchIfEmpty { Mono.error { ApplicationException(ExceptionEnum.EMPTY_DATA) } }
            .flatMap { qrService.confirm(it.token, it.status) }
            .then(ok("Share login success"))
    }


    fun loginWithToken(request: ServerRequest): Mono<ServerResponse?> {
        return request.bodyToMono<TokenRequest>()
            .switchIfEmpty { Mono.error { ApplicationException(ExceptionEnum.EMPTY_DATA) } }
            .flatMap { qrService.login(it.token) }
            .flatMap { tokenResponse ->
                ok(null, "Login success") { header ->
                    val refreshTokenCookie =
                        ResponseCookie.from("REFRESH_TOKEN", tokenResponse.refreshToken).apply {
                            maxAge(refreshTokenProperty.expires * 60000)
                            httpOnly(true)
                            secure(true)
                            path("/")
                        }.build()

                    header.add("Authorization", "Bearer ${tokenResponse.token}")
                    header.add("Set-Cookie", refreshTokenCookie.toString())
                }
            }
    }
}