/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 9:14 PM-25/07/2025
 *  User: kimin
 **/

package com.lamnguyen.auth.configs

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.security.SecurityScheme
import org.springframework.context.annotation.Configuration

@SecurityScheme(
    name = "bearer-auth",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT"
)
@SecurityScheme(
    name = "cookie-auth",
    type = SecuritySchemeType.APIKEY,
    `in` = SecuritySchemeIn.COOKIE
)
@Configuration
class SwaggerConfig