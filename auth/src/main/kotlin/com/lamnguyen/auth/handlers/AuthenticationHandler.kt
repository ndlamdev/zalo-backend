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
import com.lamnguyen.auth.service.business.IAuthService
import com.lamnguyen.auth.utils.helpers.ok
import com.lamnguyen.auth.utils.helpers.validate
import com.lamnguyen.auth.utils.properties.ApplicationProperty
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
    val authProperty: ApplicationProperty.Companion.AuthProperty
) {
    fun login(request: ServerRequest): Mono<ServerResponse?> {
        return ok(
            mapOf(
                "phone_number_code" to request.attributes()["PHONE_NUMBER_CODE"],
                "phone_number" to request.attributes()["PHONE_NUMBER"],
                "access_token" to request.attributes()["ACCESS_TOKEN"],
            )
        )
    }

    fun register(request: ServerRequest): Mono<ServerResponse?> {
        return request.bodyToMono(RegisterRequest::class.java)
            .flatMap { it ->
                validator.validate(it, authService::register)
            }
            .then(ok("Register success!"))
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
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
}