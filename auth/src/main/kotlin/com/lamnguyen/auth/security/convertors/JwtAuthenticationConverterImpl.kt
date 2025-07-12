/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 1:50 PM-08/07/2025
 *  User: kimin
 **/

package com.lamnguyen.auth.security.convertors

import com.lamnguyen.auth.domain.dto.JWTPayload
import com.lamnguyen.auth.repositories.IPermissionRepository
import com.lamnguyen.auth.utils.Keyword
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
    private val permissionRepository: IPermissionRepository
) : Converter<Jwt, Mono<AbstractAuthenticationToken>> {
    override fun convert(source: Jwt): Mono<AbstractAuthenticationToken>? {
        val body = source.getClaim<JWTPayload>("payload")

        return Flux.fromIterable(body.roles.orEmpty())
            .flatMap { roleName ->
                val trimmed = roleName?.removePrefix(Keyword.PREFIX_ROLE.value).orEmpty()

                val authorities = mutableListOf<SimpleGrantedAuthority>()
                authorities.add(SimpleGrantedAuthority(roleName ?: ""))

                val permissionsFlux = permissionRepository.findAllByRoleName(trimmed)
                    .map { permission -> SimpleGrantedAuthority(permission.name) }

                Flux.concat(Flux.fromIterable(authorities), permissionsFlux)
            }
            .collectList()
            .map { authorities -> JwtAuthenticationToken(source, authorities) }
    }
}