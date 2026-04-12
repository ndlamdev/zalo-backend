/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 3:33 PM-08/07/2025
 *  User: kimin
 **/

package com.lamnguyen.auth.utils.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties("application")
class ApplicationProperty {
    lateinit var whitelist: List<String>
    lateinit var version: String

    companion object {
        @Component
        @ConfigurationProperties("application.auth")
        class AuthProperty {
            lateinit var userPhoneNumber: String
            lateinit var userRoles: String

            companion object {
                @Component
                @ConfigurationProperties("application.auth.jwt")
                class JwtProperty {
                    lateinit var publicKey: String
                    lateinit var privateKey: String
                    lateinit var iss: String
                    lateinit var claimKey: String

                    companion object {
                        @Component
                        @ConfigurationProperties("application.auth.jwt.access-token")
                        class AccessTokenProperty {
                            var expires: Long = 0
                        }

                        @Component
                        @ConfigurationProperties("application.auth.jwt.refresh-token")
                        class RefreshTokenProperty {
                            var expires: Long = 0
                        }
                    }
                }
            }
        }
    }
}