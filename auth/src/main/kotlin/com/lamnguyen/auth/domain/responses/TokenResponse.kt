package com.lamnguyen.auth.domain.responses

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 12:31 PM-03/08/2025
 *  User: kimin
 **/
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
class TokenResponse(
    val phoneNumber: Long,
    val phoneNumberCode: Int,
    val accessToken: String,
    @JsonIgnore
    val refreshToken: String
)