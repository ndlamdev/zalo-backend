/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 2:31 PM-08/07/2025
 *  User: kimin
 **/

package com.lamnguyen.auth.domain.dto

import com.lamnguyen.auth.utils.enums.JwtTokenType

class RefreshTokenPayload : SimplePayload() {
    var accessTokenId: String? = null

    companion object {
        fun generateToken(phoneNumber: String): RefreshTokenPayload {
            return RefreshTokenPayload().apply {
                this@apply.phoneNumber = phoneNumber
                this.type = JwtTokenType.REFRESH
            }
        }
        fun generateToken(phoneNumber: String, accessTokenId: String): RefreshTokenPayload {
            return RefreshTokenPayload().apply {
                this@apply.phoneNumber = phoneNumber
                this@apply.accessTokenId = accessTokenId
                this.type = JwtTokenType.REFRESH
            }
        }
    }
}