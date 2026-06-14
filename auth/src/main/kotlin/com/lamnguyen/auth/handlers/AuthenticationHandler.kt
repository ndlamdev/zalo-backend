/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 8:55 AM-09/07/2025
 *  User: kimin
 **/

package com.lamnguyen.auth.handlers

import com.lamnguyen.auth.domain.requests.PhoneNumberRequest
import com.lamnguyen.auth.domain.requests.RegisterRequest
import com.lamnguyen.auth.domain.requests.OtpRequest
import com.lamnguyen.auth.exceptions.ApplicationException
import com.lamnguyen.auth.exceptions.ExceptionEnum
import com.lamnguyen.auth.service.business.IAuthService
import com.lamnguyen.auth.utils.enums.Keyword
import com.lamnguyen.auth.utils.helpers.JwtHelper
import com.lamnguyen.auth.utils.helpers.error
import com.lamnguyen.auth.utils.helpers.ok
import com.lamnguyen.auth.utils.helpers.validate
import com.lamnguyen.auth.utils.properties.ApplicationProperty
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseCookie
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Component
import org.springframework.validation.Validator
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyToMono
import reactor.core.publisher.Mono

/**
 * Xử lý các HTTP request liên quan đến xác thực người dùng.
 *
 * Handler này nhận dữ liệu từ request, validate payload, gọi auth service
 * và chuẩn hóa response/cookie/header trả về cho client.
 */
@Component
class AuthenticationHandler(
    val authService: IAuthService,
    val validator: Validator,
    val authProperty: ApplicationProperty.Companion.AuthProperty,
    val refreshTokenProperty: ApplicationProperty.Companion.AuthProperty.Companion.JwtProperty.Companion.RefreshTokenProperty,
    private val jwtHelper: JwtHelper,
) {
    /**
     * Trả kết quả đăng nhập đã được filter xác thực xử lý trước đó.
     *
     * Token response được lấy từ request attribute TOKEN_RESPONSE. Nếu không có dữ liệu,
     * request được xem là đăng nhập thất bại.
     */
    fun login(request: ServerRequest): Mono<ServerResponse?> {
        val response = request.attributes()["TOKEN_RESPONSE"]
            ?: return error(ApplicationException(ExceptionEnum.LOGIN_FAILED), null, null, null)

        return ok(response)
    }

    /**
     * Đăng ký tài khoản mới từ request body.
     *
     * Payload được validate trước khi chuyển sang auth service để tạo tài khoản.
     */
    fun register(request: ServerRequest): Mono<ServerResponse?> {
        return request.bodyToMono<RegisterRequest>()
            .flatMap {
                validator.validate(it, authService::register)
            }
            .then(ok("Register successfully!"))
    }

    /**
     * Gửi OTP đến số điện thoại trong request body.
     */
    fun sendOtp(request: ServerRequest): Mono<ServerResponse?> {
        return request.bodyToMono<PhoneNumberRequest>()
            .flatMap { authService.sentOtp(it.phoneNumber) }
            .then(ok("Send otp successfully!"))
    }

    /**
     * Xác thật số điện thoại đã đăng ký mới bằng mã otp đã được gửi.
     */
    fun validateAccount(request: ServerRequest): Mono<ServerResponse?> {
        return request.bodyToMono<OtpRequest>()
            .flatMap{ authService.validateAccount(it.phoneNumber, it.otp) }
            .then(ok("Validate otp successfully!"))
    }

    /**
     * Kiểm tra access token hiện tại và trả thông tin user qua response header.
     *
     * Endpoint yêu cầu quyền ROLE_USER hoặc ROLE_ADMIN.
     */
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    fun validate(request: ServerRequest): Mono<ServerResponse?> {
        return ReactiveSecurityContextHolder.getContext().flatMap { securityContext ->
            val auth = securityContext.authentication as JwtAuthenticationToken
            return@flatMap ok(null) { it ->
                it.addAll(authProperty.userRoles, auth.authorities.map { it -> it.authority })
                it.add(authProperty.userPhoneNumber, auth.name)
            }
        }
    }

    /**
     * Kiểm tra số điện thoại đã tồn tại trong hệ thống hay chưa.
     */
    fun checkPhoneNumber(request: ServerRequest): Mono<ServerResponse?> {
        return request.bodyToMono<PhoneNumberRequest>()
            .flatMap {
                validator.validate(it) { request ->
                    authService.existPhoneNumber(request.phoneNumber)
                }
            }.flatMap {
                ok(it)
            }
    }

    /**
     * Cấp lại token bằng refresh token được gửi qua cookie.
     *
     * Refresh token mới sẽ được ghi lại vào cookie bảo mật của response.
     */
    fun reSignIn(request: ServerRequest): Mono<ServerResponse?> {
        val refreshToken = request.cookies().getOrDefault(Keyword.REFRESH_TOKEN.value, null)?.get(0) ?: return error(
            ApplicationException(ExceptionEnum.MISSING_REFRESH_TOKEN),
            null,
            null,
            null
        )
        return authService.resign(refreshToken.value)
            .flatMap { tokenResponse ->
                val refreshTokenCookie =
                    ResponseCookie.from(Keyword.REFRESH_TOKEN.value, tokenResponse.refreshToken).apply {
                        maxAge(refreshTokenProperty.expires * 60000)
                        httpOnly(true)
                        secure(true)
                    }.build()
                ok(tokenResponse) {
                    it.add(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                }
            }
    }

    /**
     * Đăng xuất phiên hiện tại bằng refresh token trong cookie.
     *
     * Sau khi service thu hồi token, cookie refresh token sẽ được xóa trên client.
     */
    fun logout(request: ServerRequest): Mono<ServerResponse?> {
        val refreshToken = request.cookies().getOrDefault(Keyword.REFRESH_TOKEN.value, null)?.get(0) ?: return error(
            ApplicationException(ExceptionEnum.MISSING_REFRESH_TOKEN),
            null,
            null,
            null
        )
        return authService.logout(refreshToken.value)
            .then(ok(null, "Logout successfully!") {
                it.add(HttpHeaders.SET_COOKIE, ResponseCookie.from(Keyword.REFRESH_TOKEN.value).apply {
                    maxAge(0)
                    httpOnly(true)
                    secure(true)
                    path("/")
                }.build().value)
            })
    }

    /**
     * Trả thông tin cơ bản của người dùng đã xác thực.
     *
     * Endpoint yêu cầu quyền ROLE_USER hoặc ROLE_ADMIN.
     */
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    fun info(request: ServerRequest): Mono<ServerResponse?> {
        return ok("Get info user")
    }
}
