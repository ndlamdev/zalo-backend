/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 2:31 PM-08/07/2025
 *  User: kimin
 **/

package com.lamnguyen.auth.domain.dto

import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.security.core.Authentication
import java.util.stream.Collectors

class AccessTokenPayload : SimplePayload() {
    var refreshTokenId: String? = null

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    var roles: MutableSet<String?>? = null

    companion object {
        fun generateToken(phoneNumber: String, roles: List<String>, refreshTokenId: String): AccessTokenPayload {
            return AccessTokenPayload().apply {
                this@apply.phoneNumber = phoneNumber
                this@apply.refreshTokenId = refreshTokenId
                this@apply.roles = roles.stream()
                    .collect(Collectors.toSet())
            }
        }

        fun generateToken(authentication: Authentication, refreshTokenId: String?): AccessTokenPayload {
            return AccessTokenPayload().apply {
                phoneNumber = authentication.name
                this@apply.refreshTokenId = refreshTokenId
                this@apply.roles =
                    authentication.authorities.map { it -> it.authority }.stream().collect(Collectors.toSet())
            }
        }
    }
}