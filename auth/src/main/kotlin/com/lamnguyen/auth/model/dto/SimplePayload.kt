/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 2:30 PM-08/07/2025
 *  User: kimin
 **/

package com.lamnguyen.auth.model.dto

import com.lamnguyen.auth.utils.enums.JwtTokenType

open class SimplePayload {
    var phoneNumber: String = ""
    var type: JwtTokenType? = JwtTokenType.ACCESS
    var email: String? = null
}