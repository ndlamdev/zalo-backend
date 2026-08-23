/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 10:01 AM-19/07/2026
 *  User: ndlamdev
 **/

package com.lamnguyen.user.controller

import com.lamnguyen.user.domain.dto.UserInRelationShip
import com.lamnguyen.user.domain.request.UserInfoRequest
import com.lamnguyen.user.models.User
import com.lamnguyen.user.services.business.IUserService
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.stereotype.Controller
import reactor.core.publisher.Flux

@Controller
class RSocketController(val userService: IUserService) {
    @MessageMapping("user.friendship.info")
    fun getUserInfoFriendShip(request: UserInfoRequest): Flux<UserInRelationShip> {
        return userService.getInfoAndFriendShip(request.phoneNumber, request.members)
    }

    @MessageMapping("user.info")
    fun getUserInfo(listPhone: List<String>): Flux<User> {
        return userService.getAllInfo(listPhone)
    }
}