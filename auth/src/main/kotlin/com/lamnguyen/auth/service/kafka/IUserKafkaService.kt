/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 4:33 PM-14/07/2025
 *  User: kimin
 **/

package com.lamnguyen.auth.service.kafka

import reactor.core.publisher.Mono

interface IUserKafkaService {
    fun createUser(phoneNumber: String): Mono<Void>
}