/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 2:30 PM-08/07/2025
 *  User: kimin
 **/

package com.lamnguyen.chat.domain.dto

import com.lamnguyen.chat.utils.enums.JwtTokenType

open class SimplePayload {
    var phoneNumber: String = ""
    var type: JwtTokenType? = JwtTokenType.ACCESS
}