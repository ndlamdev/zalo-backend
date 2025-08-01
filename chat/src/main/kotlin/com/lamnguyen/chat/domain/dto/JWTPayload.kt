/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 2:31 PM-08/07/2025
 *  User: kimin
 **/

package com.lamnguyen.chat.domain.dto

import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import java.util.stream.Collectors

class JWTPayload : SimplePayload() {
    var refreshTokenId: String? = null

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    var roles: MutableSet<String?>? = null

    companion object {
        fun generateForAccessToken(authentication: Authentication): JWTPayload {
            return JWTPayload().apply {
                phoneNumber = authentication.name
                roles = authentication.authorities.stream().map { obj: GrantedAuthority? -> obj!!.authority }
                    .collect(Collectors.toSet())
            }
        }

        fun generateForAccessToken(authentication: Authentication, refreshTokenId: String?): JWTPayload {
            return JWTPayload().apply {
                phoneNumber = authentication.name
                this@apply.refreshTokenId = refreshTokenId
                this@apply.roles =
                    authentication.authorities.map { it -> it.authority }.stream().collect(Collectors.toSet())
            }
        }
    }
}