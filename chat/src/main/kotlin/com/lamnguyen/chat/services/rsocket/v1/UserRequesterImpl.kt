/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 9:19 AM-05/07/2026
 *  User: ndlamdev
 **/

package com.lamnguyen.chat.services.rsocket.v1

import com.lamnguyen.chat.domain.dto.UserDto
import com.lamnguyen.chat.domain.requests.UserInfoRequest
import com.lamnguyen.chat.services.rsocket.IUserRequester
import com.lamnguyen.chat.services.rsocket.RSocketRequesterManager
import com.lamnguyen.chat.utils.enums.RSocketServerName
import org.springframework.messaging.rsocket.retrieveFlux
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class UserRequesterImpl(rSocketRequesterManager: RSocketRequesterManager) : IUserRequester {
    private val rSocketRequester = rSocketRequesterManager.get(RSocketServerName.UserServiceRequester)

    override fun getUserInfoFriendShip(
        phoneNumber: String,
        members: List<String>
    ): Flux<UserDto> {
        return rSocketRequester?.route("user.friendship.info")?.data(UserInfoRequest(phoneNumber, members))
            ?.retrieveFlux<UserDto>() ?: Flux.empty()
    }

}