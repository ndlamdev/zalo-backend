/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 6:23 AM-10/07/2025
 *  User: kimin
 **/

package com.lamnguyen.auth.model.requests

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
class RegisterRequest {
    lateinit var phoneNumber: String
    lateinit var password: String
}