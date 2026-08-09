package com.lamnguyen.chat.services.business

import com.lamnguyen.chat.domain.dto.MemberIdResult
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface IMemberService {
    fun findAllMemberId(conversationId: String): Flux<MemberIdResult>
    fun findMemberId(conversationId: String, phoneNumber: String): Mono<MemberIdResult>
}