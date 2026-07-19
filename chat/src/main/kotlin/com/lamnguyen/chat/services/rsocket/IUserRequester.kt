/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 9:46 AM-01/08/2025
 *  User: kimin
 **/

package com.lamnguyen.chat.services.rsocket

import com.lamnguyen.chat.domain.dto.UserDto
import reactor.core.publisher.Flux

interface IUserRequester {
    fun getUserInfoFriendShip(
        phoneNumber: String,
        members: List<String>
    ): Flux<UserDto>
}