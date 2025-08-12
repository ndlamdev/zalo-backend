package com.lamnguyen.auth.service.business.v1

import com.lamnguyen.auth.repositories.IUserRepository
import com.lamnguyen.auth.service.business.IRoleService
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
class ReactiveUserDetailsServiceImpl(val userRepository: IUserRepository, val roleService: IRoleService) :
    ReactiveUserDetailsService {
    override fun findByUsername(username: String?): Mono<UserDetails?>? {
        val phoneNumber = formatPhoneNumber(username)
        val userMono = userRepository.findByPhoneNumber(phoneNumber)
        val rolesFlux: Flux<SimpleGrantedAuthority> =
            roleService.getRoles(phoneNumber)
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