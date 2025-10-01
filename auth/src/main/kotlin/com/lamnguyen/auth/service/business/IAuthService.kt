package com.lamnguyen.auth.service.business

import com.lamnguyen.auth.domain.dto.RefreshTokenPayload
import com.lamnguyen.auth.domain.requests.RegisterRequest
import com.lamnguyen.auth.domain.responses.TokenResponse
import reactor.core.publisher.Mono

interface IAuthService {
    fun register(data: RegisterRequest): Mono<Void>
    fun hasPhoneNumber(phoneNumber: String?): Mono<Void>
    fun resign(refreshToken: String): Mono<TokenResponse>
    fun logout(refreshToken: String): Mono<Void>
    fun resign(refreshToken: Mono<RefreshTokenPayload>): Mono<TokenResponse>
}