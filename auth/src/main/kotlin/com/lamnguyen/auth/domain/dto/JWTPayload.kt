/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 2:31 PM-08/07/2025
 *  User: kimin
 **/

package com.lamnguyen.auth.domain.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.lamnguyen.auth.model.User
import com.lamnguyen.auth.utils.enums.JwtTokenType
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import java.util.stream.Collectors

class JWTPayload : SimplePayload() {
    var refreshTokenId: String? = null

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    var roles: MutableSet<String?>? = null

    fun generateForAccessToken(authentication: Authentication): JWTPayload {
        return JWTPayload().apply {
            email = authentication.name
            type = JwtTokenType.ACCESS
            roles = authentication.authorities.stream().map { obj: GrantedAuthority? -> obj!!.authority }
                .collect(Collectors.toSet())
        }
    }

    fun generateForAccessToken(user: User, roles: List<String>, refreshTokenId: String?): JWTPayload {
        return JWTPayload().apply {
            phoneNumber = user.phoneNumber
            email = user.email
            this@JWTPayload.refreshTokenId = refreshTokenId
            this@JWTPayload.roles = roles.stream().collect(Collectors.toSet())
        }
    }
}