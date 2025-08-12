/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 1:50 PM-08/07/2025
 *  User: kimin
 **/

package com.lamnguyen.auth.security.convertors

import com.fasterxml.jackson.databind.ObjectMapper
import com.lamnguyen.auth.domain.dto.AccessTokenPayload
import com.lamnguyen.auth.service.business.IPermissionService
import com.lamnguyen.auth.utils.enums.Keyword
import com.lamnguyen.auth.utils.properties.ApplicationProperty
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Configuration
class JwtAuthenticationConverterImpl(
    private val permissionService: IPermissionService,
    private val jwtProperty: ApplicationProperty.Companion.AuthProperty.Companion.JwtProperty,
    private val objectMapper: ObjectMapper
) : Converter<Jwt, Mono<AbstractAuthenticationToken>> {
    override fun convert(source: Jwt): Mono<AbstractAuthenticationToken>? {
        val body = source.getClaim<Map<String, Any>>(jwtProperty.claimKey)
        val payload = objectMapper.convertValue(body, AccessTokenPayload::class.java)

        return Flux.fromIterable(payload.roles.orEmpty())
            .flatMap { roleName ->
                val trimmed = roleName?.removePrefix(Keyword.PREFIX_ROLE.value).orEmpty()

                val authorities = mutableListOf<SimpleGrantedAuthority>()
                authorities.add(SimpleGrantedAuthority(roleName ?: ""))

                val permissionsFlux = permissionService.getPermissions(trimmed)
                    .map { permission -> SimpleGrantedAuthority(permission.name) }

                Flux.concat(Flux.fromIterable(authorities), permissionsFlux)
            }
            .collectList()
            .map { authorities -> JwtAuthenticationToken(source, authorities) }
    }
}