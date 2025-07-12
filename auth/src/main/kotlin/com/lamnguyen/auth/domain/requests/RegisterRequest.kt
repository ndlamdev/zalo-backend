/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 6:23 AM-10/07/2025
 *  User: kimin
 **/

package com.lamnguyen.auth.domain.requests

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.lamnguyen.auth.utils.annotations.ValidPhoneNumber
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
class RegisterRequest {
    @NotBlank
    @NotNull
    @ValidPhoneNumber
    var phoneNumber: String? = null

    @NotBlank
    @NotNull
    var password: String? = null
}