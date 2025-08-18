/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 12:52 PM-24/07/2025
 *  User: kimin
 **/

package com.lamnguyen.user.services.business.v1

import com.lamnguyen.user.events.InviteAddFriendEvent
import com.lamnguyen.user.exceptions.ApplicationException
import com.lamnguyen.user.exceptions.ExceptionEnum
import com.lamnguyen.user.models.InviteAddFriend
import com.lamnguyen.user.repositories.IInviteAddFriendRepository
import com.lamnguyen.user.services.business.IFriendShipService
import com.lamnguyen.user.services.business.IInviteAddFriendService
import com.lamnguyen.user.services.kafka.INotificationKafkaProducer
import formatPhoneNumber
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class InviteAddFriendServiceImpl(
    val inviteAddFriendRepository: IInviteAddFriendRepository,
    val notificationKafkaProducer: INotificationKafkaProducer,
    val friendService: IFriendShipService
) : IInviteAddFriendService {
    override fun sendRequest(phoneNumberReceiver: String, message: String?): Mono<Void> {
        val phoneNumberReceiverFormated = formatPhoneNumber(phoneNumberReceiver)
        return ReactiveSecurityContextHolder
            .getContext()
            .filter { securityContext -> securityContext.authentication.name != phoneNumberReceiverFormated }
            .flatMap { securityContext ->
                inviteAddFriend(
                    securityContext.authentication.name,
                    phoneNumberReceiverFormated,
                    message
                )
            }
            .switchIfEmpty(
                Mono.error(
                    ApplicationException(
                        ExceptionEnum.INVITE_ADD_FRIEND_FAILED,
                        "The phone number that sent the friend request is the same as your phone number"
                    )
                )
            )
            .then()
    }

    private fun inviteAddFriend(
        phoneNumberSender: String,
        phoneNumberReceiver: String,
        message: String?
    ): Mono<InviteAddFriend> {
        val data = InviteAddFriend().apply {
            this.phoneNumberReceiver = formatPhoneNumber(phoneNumberReceiver)
            this.phoneNumberSender = formatPhoneNumber(phoneNumberSender)
            this.message = message
        }
        return inviteAddFriendRepository
            .existsInviteAddFriendByPhoneNumberAndDeletedIsFalse(
                formatPhoneNumber(phoneNumberReceiver),
                formatPhoneNumber(phoneNumberSender)
            )
            .filter { !it }
            .switchIfEmpty(Mono.error(ApplicationException(ExceptionEnum.INVITE_EXISTS)))
            .flatMap {
                inviteAddFriendRepository.save(data)
                    .flatMap { it ->
                        notificationKafkaProducer.sendNotification(InviteAddFriendEvent(phoneNumberSender))
                        Mono.just(it)
                    }
                    .onErrorResume { error ->
                        Mono.error(ApplicationException(ExceptionEnum.INVITE_ADD_FRIEND_FAILED))
                    }.switchIfEmpty(Mono.error(ApplicationException(ExceptionEnum.USER_NOT_FOUND)))
            }
    }

    override fun findInviteAddFriend(
        phoneNumberSender: String,
        phoneNumberReceiver: String
    ): Mono<InviteAddFriend> {
        return inviteAddFriendRepository.findInviteAddFriendAndDeletedIsFalse(
            formatPhoneNumber(phoneNumberSender),
            formatPhoneNumber(phoneNumberReceiver)
        )
    }

    override fun replyInvite(id: Long, answer: Boolean): Mono<Void> {
        return inviteAddFriendRepository.findByIdAndDeletedIsFalse(id)
            .switchIfEmpty(Mono.error(ApplicationException(ExceptionEnum.INVITE_NOT_EXISTS)))
            .flatMap { invite ->
                return@flatMap Mono.zip(
                    inviteAddFriendRepository.save(invite.apply {
                        this.accepted = answer
                        this.deleted = true
                    }),
                    friendService.addFriend(invite.phoneNumberSender, invite.phoneNumberReceiver)
                )
            }
            .then()
    }

    override fun getAllInvite(phoneNumberReceiver: String): Flux<InviteAddFriend> {
        return inviteAddFriendRepository.findAllByPhoneNumberReceiverAndDeletedIsFalse(phoneNumberReceiver)
    }

    override fun getAllRequest(phoneNumberSender: String): Flux<InviteAddFriend> {
        return inviteAddFriendRepository.findAllByPhoneNumberSenderAndDeletedIsFalse(phoneNumberSender)
    }
}