package com.lamnguyen.user.domain.request

import jakarta.validation.constraints.NotBlank
import java.time.LocalDate

class RegisInfoRequest() {
    @NotBlank
    var fullName: String? = null
    var avatarUrl: String? = null
    var birthDate: LocalDate? = null
    var email: String? = null
}