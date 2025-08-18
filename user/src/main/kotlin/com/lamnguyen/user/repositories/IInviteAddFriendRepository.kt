/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 12:50 PM-24/07/2025
 *  User: kimin
 **/

package com.lamnguyen.user.repositories

import com.lamnguyen.user.models.InviteAddFriend
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
interface IInviteAddFriendRepository : R2dbcRepository<InviteAddFriend, Long> {
    @Query(
        value = """
        SELECT * 
        FROM "zalo-user".public.invite_add_friends 
        WHERE (phone_number_sender = :phoneNumberA and phone_number_receiver = :phoneNumberB) 
            or (phone_number_sender = :phoneNumberB and phone_number_receiver = :phoneNumberA)
    """
    )
    fun findInviteAddFriendAndDeletedIsFalse(phoneNumberA: String, phoneNumberB: String): Mono<InviteAddFriend>
    fun findAllByPhoneNumberReceiverAndDeletedIsFalse(phoneNumberReceiver: String): Flux<InviteAddFriend>
    fun findAllByPhoneNumberSenderAndDeletedIsFalse(phoneNumberSender: String): Flux<InviteAddFriend>

    @Query(
        value = """
        SELECT CASE
           WHEN EXISTS (SELECT *
                        FROM "zalo-user".public.invite_add_friends
                        WHERE (phone_number_sender = :phoneNumberSender and
                               phone_number_receiver = :phoneNumberReceiver)
                           or (phone_number_sender = :phoneNumberReceiver and
                               phone_number_receiver = :phoneNumberSender)
                            AND deleted = false)
               THEN TRUE
           ELSE FALSE
           END
    """
    )
    fun existsInviteAddFriendByPhoneNumberAndDeletedIsFalse(
        phoneNumberA: String,
        phoneNumberB: String
    ): Mono<Boolean>

    fun findByIdAndDeletedIsFalse(id: Long): Mono<InviteAddFriend>
}