/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 12:59 PM-26/08/2025
 *  User: kimin
 **/

package com.lamnguyen.chat.services.kafka.v1

import com.lamnguyen.chat.entities.Message
import com.lamnguyen.chat.services.business.IMessageService
import com.lamnguyen.chat.services.kafka.IMessageConsumer
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.stereotype.Service

@Service
class MessageConsumerImpl(
    val messageService: IMessageService
) : IMessageConsumer {
    override fun saveMessage(consumerRecord: ConsumerRecord<String, Message>) {
        val message = consumerRecord.value()

        messageService.save(message)
            .subscribe()
    }
}