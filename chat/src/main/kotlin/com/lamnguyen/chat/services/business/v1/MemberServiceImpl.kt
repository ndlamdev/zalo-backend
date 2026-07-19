/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 1:09 PM-05/07/2026
 *  User: ndlamdev
 **/

package com.lamnguyen.chat.services.business.v1

import com.fasterxml.uuid.Generators
import com.lamnguyen.chat.entities.Member
import com.lamnguyen.chat.repositories.IMemberRepository
import com.lamnguyen.chat.services.business.IMemberService
import com.lamnguyen.chat.utils.enums.MemberRole
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class MemberServiceImpl(val memberRepository: IMemberRepository) : IMemberService {
//    override fun addMember(
//        conversationId: String,
//        userId: String,
//    ): Mono<Member> {
//        val memberId = Generators.timeBasedEpochGenerator().generate()
//
//        val member = Member().apply {
//            id = memberId.toString()
//            this.conversationId = conversationId
//            this.userId = userId
//            this.role = role ?: MemberRole.USER
//        }
//
//        return memberRepository.save(memberRepository)
//    }

    override fun addMembersRoleUser(
        conversationId: String,
        joinBy: String,
        userIds: List<String>
    ): Flux<Member> {
        val users = userIds.map { userId ->
            Member().apply {
                id = Generators.timeBasedEpochGenerator().generate().toString()
                this.conversationId = conversationId
                this.userId = userId
                this.role = MemberRole.USER
                this.joinedBy = joinBy
                isNewItem = true
            }
        }

        return memberRepository.saveAll(users)
    }

    override fun addMemberRoleAdmin(
        conversationId: String,
        memberId: String,
        userId: String
    ): Mono<Member> {

        val member = Member().apply {
            id = memberId
            this.conversationId = conversationId
            this.userId = userId
            this.role = MemberRole.ADMIN
            this.joinedBy = memberId
            isNewItem = true
        }

        return memberRepository.save(member)
    }

    override fun getMembersInConversation(conversationId: String): Flux<Member> {
        return memberRepository.findAllByConversationId(conversationId)
    }
}