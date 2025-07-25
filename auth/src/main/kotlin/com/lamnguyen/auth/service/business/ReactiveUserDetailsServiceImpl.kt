package com.lamnguyen.auth.service.business

import com.lamnguyen.auth.repositories.IRoleRepository
import com.lamnguyen.auth.repositories.IUserRepository
import com.lamnguyen.auth.utils.enums.Keyword
import formatPhoneNumber
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 6:03 AM-09/07/2025
 *  User: kimin
 **/
@Component
class ReactiveUserDetailsServiceImpl(val userRepository: IUserRepository, val roleRepository: IRoleRepository) :
    ReactiveUserDetailsService {
    override fun findByUsername(username: String?): Mono<UserDetails?>? {
        val phoneNumber = formatPhoneNumber(username)
        if (phoneNumber == null) return null
        val userMono = userRepository.findByPhoneNumber(phoneNumber)
        val rolesFlux: Flux<SimpleGrantedAuthority> =
            roleRepository.findByUserPhoneNumber(phoneNumber)
                .map { role -> SimpleGrantedAuthority("${Keyword.PREFIX_ROLE.value}${role?.name}") }

        return Mono.zip(userMono, rolesFlux.collectList())
            .mapNotNull { tuple ->
                val user = tuple.t1
                if (user == null) return@mapNotNull null
                val authorities = tuple.t2
                User(user.phoneNumber, user.password, user.active, true, true, true, authorities)
            }
    }
}