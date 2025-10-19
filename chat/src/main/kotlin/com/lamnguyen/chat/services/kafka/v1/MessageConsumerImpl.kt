/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 12:59 PM-26/08/2025
 *  User: kimin
 **/

package com.lamnguyen.chat.services.kafka.v1

import com.lamnguyen.chat.entities.Message
import com.lamnguyen.chat.entities.RoomChat
import com.lamnguyen.chat.entities.RoomChatMember
import com.lamnguyen.chat.services.business.IMessageService
import com.lamnguyen.chat.services.business.IRoomChatMemberService
import com.lamnguyen.chat.services.business.IRoomChatService
import com.lamnguyen.chat.services.grpc.IUserGrpcService
import com.lamnguyen.chat.services.kafka.IMessageConsumer
import com.lamnguyen.chat.services.kafka.IRoomChatMemberProducer
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.util.*

@Service
class MessageConsumerImpl(
    val roomChatService: IRoomChatService,
    val messageService: IMessageService,
    val userGrpcService: IUserGrpcService,
    val roomChatMemberService: IRoomChatMemberService,
    val roomChatMemberProducerImpl: IRoomChatMemberProducer,
) : IMessageConsumer {
    override fun saveMessage(consumerRecord: ConsumerRecord<String, Message>) {
        val token = String(consumerRecord.headers().lastHeader(HttpHeaders.AUTHORIZATION).value())
        val message = consumerRecord.value()
        if (message.roomChatId.startsWith("+")) {
            val roomChatIdReceiver = "${message.roomChatId}_${message.senderPhoneNumber}"
            val roomChatIdSender = "${message.senderPhoneNumber}_${message.roomChatId}"
            Mono.zip(
                saveMessageForSender(roomChatIdSender, message.copy()),
                saveMessageForReceiver(roomChatIdReceiver, message.copy(), token),
            ).subscribe()
        } else {
            messageService.save(message)
                .subscribe()
        }
    }

    private fun saveMessageForSender(id: String, message: Message): Mono<*> {
        return roomChatService.existRoomChatById(id)
            .flatMap {
                if (it) saveMessage(message.copy(), id)
                else createRoomChatAndSaveMessage(
                    message.copy(),
                    message.senderPhoneNumber,
                    message.roomChatId,
                )
            }
    }

    private fun saveMessageForReceiver(idId: String, message: Message, token: String): Mono<*> {
        return roomChatService.existRoomChatById(idId)
            .flatMap {
                if (it) saveMessage(message, idId)
                else userGrpcService.getFriendShips(message.senderPhoneNumber, listOf(message.roomChatId), token)
                    .map { response -> response.friendsList }
                    .flatMap { listFriend ->
                        createRoomChatAndSaveMessage(
                            message,
                            message.roomChatId,
                            message.senderPhoneNumber,
                            listFriend.isEmpty()
                        )
                    }
            }
    }

    private fun createRoomChatAndSaveMessage(
        message: Message,
        admin: String,
        member: String,
        isQueue: Boolean? = false,
    ): Mono<*> {
        val id = UUID.randomUUID().toString()
        return roomChatService.createRoomChat(
            RoomChat().apply {
                this.id = id
                this.isQueue = isQueue ?: false
                newRow = true
                this.id = "${admin}_$member"
            })
            .flatMap {
                Mono.zip(
                    roomChatMemberService.addMember(id, admin, RoomChatMember.Role.ADMIN),
                    roomChatMemberService.addMember(id, member),
                    messageService.save(
                        message.apply {
                            roomChatId = id
                        }
                    )
                )
            }.doOnSuccess {
                roomChatMemberProducerImpl.dumpRoomChatMember(
                    id,
                    listOf(admin, member)
                )
            }
    }

    private fun saveMessage(message: Message, idId: String): Mono<*> {
        return roomChatService.findById(idId)
            .flatMap { roomChat ->
                messageService.save(
                    message.apply {
                        roomChatId = roomChat.id!!
                    })
            }
    }
}