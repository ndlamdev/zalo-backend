package com.lamnguyen.auth.service.business

import com.lamnguyen.auth.domain.requests.RegisterRequest
import reactor.core.publisher.Mono

interface IAuthService {
    fun register(data: RegisterRequest): Mono<Void>
    fun hasPhoneNumber(phoneNumber: String?): Mono<Void>
}