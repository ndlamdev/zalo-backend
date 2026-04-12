/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 8:55 AM-09/07/2025
 *  User: kimin
 **/

package com.lamnguyen.auth.handlers

import com.lamnguyen.auth.domain.requests.PhoneNumberRequest
import com.lamnguyen.auth.domain.requests.RegisterRequest
import com.lamnguyen.auth.exceptions.ApplicationException
import com.lamnguyen.auth.exceptions.ExceptionEnum
import com.lamnguyen.auth.service.business.IAuthService
import com.lamnguyen.auth.utils.enums.Keyword
import com.lamnguyen.auth.utils.helpers.error
import com.lamnguyen.auth.utils.helpers.ok
import com.lamnguyen.auth.utils.helpers.validate
import com.lamnguyen.auth.utils.properties.ApplicationProperty
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseCookie
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Component
import org.springframework.validation.Validator
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class AuthenticationHandler(
    val authService: IAuthService,
    val validator: Validator,
    val authProperty: ApplicationProperty.Companion.AuthProperty,
    val refreshTokenProperty: ApplicationProperty.Companion.AuthProperty.Companion.JwtProperty.Companion.RefreshTokenProperty,
) {
    fun login(request: ServerRequest): Mono<ServerResponse?> {
        val response = request.attributes()["TOKEN_RESPONSE"]

        return ok(response)
    }

    fun register(request: ServerRequest): Mono<ServerResponse?> {
        return request.bodyToMono(RegisterRequest::class.java)
            .flatMap { it ->
                validator.validate(it, authService::register)
            }
            .then(ok("Register success!"))
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    fun validate(request: ServerRequest): Mono<ServerResponse?> {
        return ReactiveSecurityContextHolder.getContext().flatMap { securityContext ->
            val auth = securityContext.authentication as JwtAuthenticationToken
            return@flatMap ok(null) { it ->
                it.addAll(authProperty.userRoles, auth.authorities.map { it -> it.authority })
                it.add(authProperty.userPhoneNumber, auth.name)
            }
        }
    }

    fun checkPhoneNumber(request: ServerRequest): Mono<ServerResponse?> {
        return request.bodyToMono(PhoneNumberRequest::class.java)
            .flatMap { it ->
                validator.validate(it) { request ->
                    authService.hasPhoneNumber(it.phoneNumber)
                }
            }.then(ok(null))
    }

    fun resign(request: ServerRequest): Mono<ServerResponse?> {
        val refreshToken = request.cookies().getOrDefault(Keyword.REFRESH_TOKEN.value, null)?.get(0)
        if (refreshToken == null)
            return error(ApplicationException(ExceptionEnum.MISSING_REFRESH_TOKEN), null, null, null)
        return authService.resign(refreshToken.value)
            .flatMap { tokenResponse ->
                val refreshTokenCookie =
                    ResponseCookie.from(Keyword.REFRESH_TOKEN.value, tokenResponse.refreshToken).apply {
                        maxAge(refreshTokenProperty.expires * 60000)
                        httpOnly(true)
                        secure(true)
                    }.build()
                ok(tokenResponse) {
                    it.add(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                }
            }
    }

    fun logout(request: ServerRequest): Mono<ServerResponse?> {
        val refreshToken = request.cookies().getOrDefault(Keyword.REFRESH_TOKEN.value, null)?.get(0)
        if (refreshToken == null)
            return error(ApplicationException(ExceptionEnum.MISSING_REFRESH_TOKEN), null, null, null)
        return authService.logout(refreshToken.value)
            .then(ok(null, "Logout success!") {
                it.add(HttpHeaders.SET_COOKIE, ResponseCookie.from(Keyword.REFRESH_TOKEN.value).apply {
                    maxAge(0)
                    httpOnly(true)
                    secure(true)
                    path("/")
                }.build().value)
            })
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    fun info(request: ServerRequest): Mono<ServerResponse?> {
        return ok("Get info user")
    }
}