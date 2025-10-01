package com.lamnguyen.auth.domain.requests

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.lamnguyen.auth.utils.annotations.ValidPhoneNumber
import jakarta.validation.constraints.NotBlank

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
class QrLoginRequest {
    @NotBlank
    lateinit var sid: String
}