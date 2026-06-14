/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 8:52 AM-09/07/2025
 *  User: kimin
 **/

package com.lamnguyen.auth.router

import com.lamnguyen.auth.domain.requests.LoginRequest
import com.lamnguyen.auth.domain.requests.PhoneNumberRequest
import com.lamnguyen.auth.domain.requests.RegisterRequest
import com.lamnguyen.auth.handlers.AuthenticationHandler
import com.lamnguyen.auth.handlers.QrHandler
import com.lamnguyen.auth.utils.PathKeyworkCommon
import com.lamnguyen.auth.utils.properties.ApplicationProperty
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springdoc.core.annotations.RouterOperation
import org.springdoc.core.annotations.RouterOperations
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse


/**
 * Cấu hình functional routes cho các API xác thực.
 *
 * Router này khai báo route cho đăng nhập, đăng ký, OTP, token lifecycle,
 * thông tin người dùng và nhóm endpoint đăng nhập bằng QR code.
 */
@Configuration(proxyBeanMethods = false)
class AuthenticationRouter {
    /**
     * Tạo RouterFunction cho toàn bộ endpoint xác thực theo version cấu hình.
     *
     * Các route được gắn với AuthenticationHandler hoặc QrHandler tương ứng,
     * đồng thời khai báo metadata OpenAPI thông qua RouterOperations.
     */
    @Bean
    @RouterOperations(
        RouterOperation(
            path = "/v1/login",
            method = [RequestMethod.POST],
            beanClass = AuthenticationHandler::class,
            beanMethod = "login",
            operation = Operation(
                operationId = "login-form",
                requestBody = RequestBody(
                    required = true,
                    content = [
                        Content(
                            mediaType = "application/json",
                            schema = Schema(implementation = LoginRequest::class)
                        )
                    ]
                )
            )
        ),
        RouterOperation(
            path = "/v1/register",
            method = [RequestMethod.POST],
            beanClass = AuthenticationHandler::class,
            beanMethod = "register",
            operation = Operation(
                operationId = "register-form",
                requestBody = RequestBody(
                    required = true,
                    content = [
                        Content(
                            mediaType = "application/json",
                            schema = Schema(implementation = RegisterRequest::class)
                        )
                    ]
                )
            )
        ),
        RouterOperation(
            path = "/v1/validate",
            method = [RequestMethod.POST],
            beanClass = AuthenticationHandler::class,
            beanMethod = "validate",
            operation = Operation(
                operationId = "validate-request",
                security = [SecurityRequirement(name = "bearer-auth")],
            )
        ),
        RouterOperation(
            path = "/v1/check-phone-number",
            method = [RequestMethod.POST],
            beanClass = AuthenticationHandler::class,
            beanMethod = "checkPhoneNumber",
            operation = Operation(
                operationId = "phone-number-form",
                requestBody = RequestBody(
                    required = true,
                    content = [
                        Content(
                            mediaType = "application/json",
                            schema = Schema(implementation = PhoneNumberRequest::class)
                        )
                    ]
                )
            ),
        ),
        RouterOperation(
            path = "/v1/resign",
            method = [RequestMethod.POST],
            beanClass = AuthenticationHandler::class,
            beanMethod = "resign",
        ),
        RouterOperation(
            path = PathKeyworkCommon.LOGIN,
            method = [RequestMethod.POST],
            beanClass = AuthenticationHandler::class,
            beanMethod = "logout",
        ),
        RouterOperation(
            path = "/v1/qr/generator",
            method = [RequestMethod.POST],
            beanClass = AuthenticationHandler::class,
            beanMethod = "logout",
        ),
    )
    fun authenticationRoute(
        authenticationHandler: AuthenticationHandler,
        applicationProperty: ApplicationProperty,
        qrHandler: QrHandler
    ): RouterFunction<ServerResponse?> {
        return RouterFunctions
            .route()
            .path("/${applicationProperty.version}") { v1 ->
                v1.POST(PathKeyworkCommon.LOGIN, authenticationHandler::login)
                v1.POST(PathKeyworkCommon.REGISTER, authenticationHandler::register)
                v1.POST(PathKeyworkCommon.REQUEST_OTP, authenticationHandler::sendOtp)
                v1.POST(PathKeyworkCommon.VALIDATE_ACCOUNT, authenticationHandler::validateAccount)
                v1.POST(PathKeyworkCommon.VALIDATE, authenticationHandler::validate)
                v1.POST(PathKeyworkCommon.CHECK_PHONE_NUMBER, authenticationHandler::checkPhoneNumber)
                v1.POST(PathKeyworkCommon.RE_SIGN_IN, authenticationHandler::reSignIn)
                v1.POST(PathKeyworkCommon.LOGOUT, authenticationHandler::logout)
                v1.GET(PathKeyworkCommon.INFO, authenticationHandler::info)
                v1.path(PathKeyworkCommon.QR) { qr ->
                    qr.GET(PathKeyworkCommon.GENERATE, qrHandler::generateQr)
                    qr.GET(PathKeyworkCommon.SUBSCRIBE, qrHandler::subscribe)
                    qr.POST(PathKeyworkCommon.SCAN, qrHandler::scan)
                    qr.POST(PathKeyworkCommon.CONFIRM, qrHandler::confirm)
                    qr.POST(PathKeyworkCommon.LOGIN, qrHandler::loginWithToken)
                }
            }
            .build()
    }
}
