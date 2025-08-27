/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 1:00 PM-26/08/2025
 *  User: kimin
 **/

package com.lamnguyen.chat.services.business.v1

import com.lamnguyen.chat.entities.Message
import com.lamnguyen.chat.repositories.IMessageRepository
import com.lamnguyen.chat.services.business.IMessageService
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class MessageServiceImpl(val messageRepository: IMessageRepository) : IMessageService {
    override fun save(message: Message): Mono<Message> {
        return messageRepository.save(message)
    }
}