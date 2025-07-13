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
    lateinit var auth: Auth

    companion object {
        class Auth {
            companion object {
                @Component
                @ConfigurationProperties("application.auth.jwt")
                class Jwt {
                    lateinit var secretKey: String
                    lateinit var iss: String
                    lateinit var claimKey: String
                    lateinit var accessToken: AccessToken

                    companion object {
                        @Component
                        @ConfigurationProperties("application.auth.jwt.access-token")
                        class AccessToken {
                            var expires: Long = 0
                        }

                        @Component
                        @ConfigurationProperties("application.auth.jwt.refresh-token")
                        class RefreshToken {
                            var expires: Long = 0
                        }
                    }
                }
            }
        }
    }
}