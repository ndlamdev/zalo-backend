package com.lamnguyen.auth.domain.requests

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
class LoginRequest {
    lateinit var phoneNumber: String
    lateinit var password: String
}