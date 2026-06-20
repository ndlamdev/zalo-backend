/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 4:57 PM-30/07/2025
 *  User: kimin
 **/

package com.lamnguyen.user.repositories

import com.lamnguyen.user.models.FriendShip
import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
interface IFriendShipRepository : R2dbcRepository<FriendShip, String> {
    fun existsFriendShipsByOwnerPhoneNumberAndFriendPhoneNumber(
        ownerPhoneNumber: String,
        friendPhoneNumber: String
    ): Mono<Boolean>

    fun findAllByOwnerPhoneNumber(
        ownerPhoneNumber: String
    ): Flux<FriendShip>
}