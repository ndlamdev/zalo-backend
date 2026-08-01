/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 9:15 AM-29/07/2025
 *  User: kimin
 **/

package com.lamnguyen.chatws.security.filters

import com.lamnguyen.chatws.domain.dto.ApiResponseError
import com.lamnguyen.chatws.utils.helpers.JwtHelper
import com.lamnguyen.chatws.utils.properties.ApplicationProperty
import com.nimbusds.jose.shaded.gson.Gson
import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.context.SecurityContextImpl


class JwtAuthenticationFilter(
    val authProperty: ApplicationProperty.Companion.AuthProperty,
    val jwtHelper: JwtHelper
) : Filter {
    override fun doFilter(
        request: ServletRequest?,
        response: ServletResponse?,
        chain: FilterChain?
    ) {
        val httpRequest = request as HttpServletRequest
        val httpResponse = response as HttpServletResponse

        val tokens = httpRequest.getHeader(HttpHeaders.AUTHORIZATION)
        if (tokens.isNullOrEmpty()) {
            chain?.doFilter(request, response)
            return
        }

        val token = tokens.substring(7)


        try {
            val authorities = httpRequest.getHeaders(authProperty.userRoles)
                .asSequence()
                .map { SimpleGrantedAuthority(it) }
                .toMutableSet()

            val authentication = jwtHelper.initAuthenticationToken(token, authorities)

            val context = SecurityContextImpl(authentication)
            SecurityContextHolder.setContext(context)

            chain?.doFilter(request, response)
        } catch (e: Exception) {
            httpResponse.status = HttpStatus.UNAUTHORIZED.value()
            httpResponse.contentType = MediaType.APPLICATION_JSON.toString()
            val bodyResponse = ApiResponseError<Any>().apply {
                code = HttpStatus.UNAUTHORIZED.value()
                error = HttpStatus.UNAUTHORIZED.reasonPhrase
                detail = e.localizedMessage
                trace = e.stackTrace
            }
            httpResponse.writer.write(Gson().toJson(bodyResponse))
        }
    }
}