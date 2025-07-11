/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 8:55 AM-09/07/2025
 *  User: kimin
 **/

package com.lamnguyen.auth.handlers

import com.lamnguyen.auth.model.requests.RegisterRequest
import com.lamnguyen.auth.service.IAuthService
import com.lamnguyen.auth.utils.helpers.ok
import com.lamnguyen.auth.utils.helpers.validate
import org.springframework.stereotype.Component
import org.springframework.validation.Validator
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class AuthenticationHandler(val authService: IAuthService, val validator: Validator) {
    fun login(request: ServerRequest): Mono<ServerResponse?> {
        return ok("Login success!")
    }

    fun register(request: ServerRequest): Mono<ServerResponse?> {
        return request.bodyToMono(RegisterRequest::class.java)
            .flatMap { it ->
                validator.validate<RegisterRequest>(it)
                    .flatMap(authService::register)
            }
            .then(ok("Register success!"))
    }
}