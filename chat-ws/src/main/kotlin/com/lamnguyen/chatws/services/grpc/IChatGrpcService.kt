/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 5:37 PM-26/08/2025
 *  User: kimin
 **/

package com.lamnguyen.chatws.services.grpc

import reactor.core.publisher.Mono

interface IChatGrpcService {
    fun getMembersInRoomChat(roomChatId: String, token: String?):  Mono<List<String>>
}