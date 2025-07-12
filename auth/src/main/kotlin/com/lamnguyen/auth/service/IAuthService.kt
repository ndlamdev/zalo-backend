package com.lamnguyen.auth.service

import com.lamnguyen.auth.domain.requests.RegisterRequest
import reactor.core.publisher.Mono

interface IAuthService {
    fun register(data: RegisterRequest): Mono<Void>
}