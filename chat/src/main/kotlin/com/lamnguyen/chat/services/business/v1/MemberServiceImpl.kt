package com.lamnguyen.chat.services.business.v1

import com.lamnguyen.chat.domain.dto.MemberIdResult
import com.lamnguyen.chat.repositories.IMemberRepository
import com.lamnguyen.chat.services.business.IMemberService
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class MemberServiceImpl(
    private val memberRepository: IMemberRepository
) : IMemberService {
    override fun findAllMemberId(conversationId: String): Flux<MemberIdResult> {
        return memberRepository.findAllMemberIdByConversationId(conversationId)
    }

    override fun findMemberId(
        conversationId: String,
        phoneNumber: String
    ): Mono<MemberIdResult> {
        return memberRepository.findMemberId(conversationId, phoneNumber)
    }
}