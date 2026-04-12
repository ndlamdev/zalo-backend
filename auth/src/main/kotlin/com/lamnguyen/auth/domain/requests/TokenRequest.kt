/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 3:06 PM-11/04/2026
 *  User: kimin
 **/

package com.lamnguyen.auth.domain.requests

import jakarta.validation.constraints.NotBlank


open class TokenRequest {
    @NotBlank
    lateinit var token: String
}