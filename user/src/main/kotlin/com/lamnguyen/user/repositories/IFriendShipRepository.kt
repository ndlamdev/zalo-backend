/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 4:57 PM-30/07/2025
 *  User: kimin
 **/

package com.lamnguyen.user.repositories

import com.lamnguyen.user.models.FriendShip
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
interface IFriendShipRepository : R2dbcRepository<FriendShip, String> {

    @Query(
        """
        SELECT CASE
           WHEN EXISTS (SELECT *
                        FROM "zalo-user".public.friend_ships
                        WHERE (phone_number_user_1 = $1 and phone_number_user_2 = $2)
                           or (phone_number_user_1 = $2 and phone_number_user_2 = $1)) THEN true
           ELSE false END
    """
    )
    fun existsFriendShips(
        phoneNumberUser1: String,
        phoneNumberUser2: String
    ): Mono<Boolean>


    @Query(
        """
        SELECT *
        FROM "zalo-user".public.friend_ships
        WHERE (phone_number_user_1 = $1 or phone_number_user_2 = $1)
    """
    )
    fun findAllByPhoneNumber(
        phoneNumber: String
    ): Flux<FriendShip>
}