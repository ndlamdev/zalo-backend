/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 1:44 PM-08/07/2025
 *  User: kimin
 **/

package com.lamnguyen.auth.security

import com.lamnguyen.auth.security.convertors.JwtAuthenticationConverterImpl
import com.lamnguyen.auth.security.entrypoint.AuthenticationEntryPoint
import com.lamnguyen.auth.security.filters.CheckBlacklistTokenFilter
import com.lamnguyen.auth.security.filters.JwtTokenGenerateFilter
import com.lamnguyen.auth.security.filters.UsernamePasswordJsonAuthenticationFilter
import com.lamnguyen.auth.utils.properties.ApplicationProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository

@Configuration
@EnableReactiveMethodSecurity
class SecurityConfig(
    val applicationProperty: ApplicationProperty,
    var jwtAuthenticationConverter: JwtAuthenticationConverterImpl,
    var jwtGenerateFilter: JwtTokenGenerateFilter,
    var manager: ReactiveAuthenticationManager,
    var checkBlacklistTokenFilter: CheckBlacklistTokenFilter,
    var authenticationEntryPoint: AuthenticationEntryPoint,
//    var removeBearerTokenAuthorizationFilter: RemoveBearerTokenAuthorizationFilter,
) {

    @Bean
    fun securityFilterChain(httpSecurity: ServerHttpSecurity): SecurityWebFilterChain? {
        httpSecurity.addFilterAt(
            UsernamePasswordJsonAuthenticationFilter("/*/login", manager),
            SecurityWebFiltersOrder.AUTHENTICATION
        )
        httpSecurity.addFilterAfter(jwtGenerateFilter, SecurityWebFiltersOrder.AUTHENTICATION)
        httpSecurity.addFilterBefore(checkBlacklistTokenFilter, SecurityWebFiltersOrder.AUTHORIZATION)
//        httpSecurity.addFilterBefore(removeBearerTokenAuthorizationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
        httpSecurity.csrf { csrf -> csrf.disable() }
        httpSecurity.authorizeExchange { exchange ->
            exchange
                .pathMatchers("/actuator/**").permitAll()
                .pathMatchers(*applicationProperty.whitelist.toTypedArray<String>()).permitAll()
                .pathMatchers("/**").permitAll()
        }
        httpSecurity.oauth2ResourceServer { oAuth2ResourceServerConfigurer ->
            oAuth2ResourceServerConfigurer.apply {
                jwt { configurer ->
                    configurer.jwtAuthenticationConverter(jwtAuthenticationConverter)
                }
                authenticationEntryPoint(authenticationEntryPoint)
            }
        }
        httpSecurity.httpBasic { httpBasic -> httpBasic.authenticationEntryPoint(authenticationEntryPoint) }
        httpSecurity.securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
        return httpSecurity.build()
    }

//    @Bean
//    fun googleAuthorizationCodeTokenRequest(): GoogleAuthorizationCodeTokenRequest? {
//        return GoogleAuthorizationCodeTokenRequest(
//            NetHttpTransport(),
//            GsonFactory(),
//            "https://oauth2.googleapis.com/token",
//            applicationProperty.getClientId(),
//            applicationProperty.getClientSecret(),
//            "",
//            "postmessage" // or your redirect URI
//        )
//    }
//
//    @Bean
//    fun googleIdTokenVerifier(): GoogleIdTokenVerifier {
//        return Builder(NetHttpTransport(), GsonFactory())
//            .setAudience(mutableListOf<T?>(applicationProperty.getClientId()))
//            .build()
//    }
}