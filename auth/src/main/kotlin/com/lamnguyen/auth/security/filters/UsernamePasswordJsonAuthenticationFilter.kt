/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 3:22 PM-08/07/2025
 *  User: kimin
 **/

package com.lamnguyen.auth.security.filters

import com.fasterxml.jackson.databind.ObjectMapper
import com.lamnguyen.auth.model.requests.LoginRequest
import org.springframework.core.io.buffer.DataBufferUtils
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher
import kotlin.text.Charsets.UTF_8

class UsernamePasswordJsonAuthenticationFilter(path: String, manage: ReactiveAuthenticationManager) :
    AuthenticationWebFilter(manage) {

    init {
        this.setRequiresAuthenticationMatcher(PathPatternParserServerWebExchangeMatcher(path, HttpMethod.POST))
        this.setServerAuthenticationConverter { ex ->
            DataBufferUtils
                .join(ex.request.body)
                .map { dataBuffer ->
                    val bodyStr = dataBuffer.toString(UTF_8)
                    val loginRequest = ObjectMapper().readValue(bodyStr, LoginRequest::class.java)
                    UsernamePasswordAuthenticationToken(loginRequest.phoneNumber, loginRequest.password)
                }
        }
    }
}