/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 1:02 PM-26/08/2025
 *  User: kimin
 **/

package com.lamnguyen.chat.repositories

import com.lamnguyen.chat.entities.Message
import org.springframework.data.repository.reactive.ReactiveCrudRepository

interface IMessageRepository : ReactiveCrudRepository<Message, String> {
}