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
import com.lamnguyen.auth.utils.annotations.PasswordMatches
import com.lamnguyen.auth.utils.annotations.ValidPhoneNumber
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

@PasswordMatches
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
class RegisterRequest {
    @NotBlank
    @NotNull
    @ValidPhoneNumber
    var phoneNumber: String? = null

    @NotBlank
    @NotNull
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(regexp = ".*[A-Z].*", message = "Password must contain at least one uppercase letter")
    @Pattern(regexp = ".*[a-z].*", message = "Password must contain at least one lowercase letter")
    @Pattern(regexp = ".*\\d.*", message = "Password must contain at least one digit")
    @Pattern(regexp = ".*[@\$!%*?&^#~_+=-].*", message = "Password must contain at least one special character (@\\\$!%*?&^#~_+=-)")
    var password: String? = null

    @NotBlank
    @NotNull
    val confirmPassword: String? = null
}