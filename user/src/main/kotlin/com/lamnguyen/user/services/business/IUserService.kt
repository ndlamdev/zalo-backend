/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 5:12 AM-15/07/2025
 *  User: kimin
 **/

package com.lamnguyen.user.services.business

import com.lamnguyen.user.domain.dto.UserInRelationShip
import com.lamnguyen.user.domain.request.RegisInfoRequest
import com.lamnguyen.user.models.User
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface IUserService {
    fun createUser(phoneNumber: String): Mono<User>
    fun getAllFriend(ownerPhoneNumber: String): Flux<UserInRelationShip>
    fun getInfo(phoneNumber: String): Mono<User>
    fun getAllInfo(listPhone: List<String>): Flux<User>
    fun registerInfo(phoneNumber: String, data: RegisInfoRequest): Mono<UserInRelationShip>
    fun findFriendAndStrangerByPhoneNumber(ownerPhoneNumber: String, phoneNumber: String): Flux<UserInRelationShip>
    fun getInfoAndFriendShip(owner: String, members: List<String>): Flux<UserInRelationShip>
}