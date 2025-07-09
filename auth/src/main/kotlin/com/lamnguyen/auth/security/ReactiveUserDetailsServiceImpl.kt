/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 6:03 AM-09/07/2025
 *  User: kimin
 **/

package com.lamnguyen.auth.security

import com.lamnguyen.auth.repositories.IRoleRepository
import com.lamnguyen.auth.repositories.IUserRepository
import com.lamnguyen.auth.utils.Keyword
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Component
class ReactiveUserDetailsServiceImpl(val userRepository: IUserRepository, val roleRepository: IRoleRepository) :
    ReactiveUserDetailsService {
    override fun findByUsername(username: String?): Mono<UserDetails?>? {
        if (username == null) return null
        val userMono = userRepository.findByPhoneNumber(username)

        val rolesFlux: Flux<SimpleGrantedAuthority> =
            roleRepository.findByUserPhoneNumber(username)
                .map { role -> SimpleGrantedAuthority("${Keyword.PREFIX_ROLE}${role?.name}") }

        return Mono.zip(userMono, rolesFlux.collectList())
            .map { tuple ->
                val user = tuple.t1
                val authorities = tuple.t2
                User(user.phoneNumber, user.password, authorities)
            }
    }
}