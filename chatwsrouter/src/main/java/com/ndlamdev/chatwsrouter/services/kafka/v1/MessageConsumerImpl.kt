/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 12:59 PM-26/08/2025
 *  User: kimin
 **/

package com.ndlamdev.chatwsrouter.services.kafka.v1

import com.ndlamdev.chatwsrouter.domain.message.ChatMessage
import com.ndlamdev.chatwsrouter.services.kafka.IMessageConsumer
import com.ndlamdev.chatwsrouter.services.rsocket.IChatWsServiceRequester
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.stereotype.Service

@Service
class MessageConsumerImpl(
    val chatWsServiceRequester: IChatWsServiceRequester
) : IMessageConsumer {
    override fun saveMessage(consumerRecord: ConsumerRecord<String, ChatMessage>) {
        val message = consumerRecord.value()

        chatWsServiceRequester.routeMessage(message).subscribe()
    }
}