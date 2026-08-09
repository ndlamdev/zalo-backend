/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 1:00 PM-26/08/2025
 *  User: kimin
 **/

package com.lamnguyen.chat.services.business

import com.lamnguyen.chat.entities.Message
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface IMessageService {
    fun save(message: Message): Mono<Message>
}