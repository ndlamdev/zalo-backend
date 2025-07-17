/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 7:49 AM-17/07/2025
 *  User: kimin
 **/

package com.lamnguyen.gateway.utils.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

class ApplicationProperty {
    companion object {
        @Component
        @ConfigurationProperties(prefix = "application.auth")
        class AuthProperty {
            lateinit var userPhoneNumber: String
            lateinit var userRoles: String
        }
    }
}