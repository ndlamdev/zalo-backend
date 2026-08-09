/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 5:17 PM-28/07/2025
 *  User: kimin
 **/

package com.lamnguyen.chat.services.business.v1

import com.lamnguyen.chat.domain.dto.ConversationDto
import com.lamnguyen.chat.domain.requests.CreateConversationRequest
import com.lamnguyen.chat.entities.Conversation
import com.lamnguyen.chat.entities.ConversationMemberMetadata
import com.lamnguyen.chat.entities.Member
import com.lamnguyen.chat.exceptions.ApplicationException
import com.lamnguyen.chat.exceptions.ExceptionEnum
import com.lamnguyen.chat.repositories.IConversationRepository
import com.lamnguyen.chat.services.business.IConversationService
import com.lamnguyen.chat.services.redis.IConversationCacheManager
import com.lamnguyen.chat.services.rsocket.IUserRequester
import com.lamnguyen.chat.utils.enums.ConversationType
import com.lamnguyen.chat.utils.enums.MemberRole
import com.lamnguyen.chat.utils.helpers.KeyGenerator
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import java.time.LocalDateTime

@Service
class ConversationServiceImpl(
    private val conversationRepository: IConversationRepository,
    private val userRequester: IUserRequester,
    private val conversationCacheManager: IConversationCacheManager,
) : IConversationService {

    @Transactional
    override fun createConversation(createConversationRequest: CreateConversationRequest): Mono<Conversation> {
        return ReactiveSecurityContextHolder.getContext()
            .map { it.authentication }
            .flatMap { auth ->
                createConversation(auth.name!!, createConversationRequest.members ?: listOf())
            }
    }

    @Transactional
    override fun createConversation(admin: String, users: List<String>, createdBy: String?): Mono<Conversation> {
        val distinctMembers = users
            .distinct()
            .filter { it.isNotBlank() && it != admin }

        if (distinctMembers.isEmpty()) return Mono.error(
            ApplicationException(ExceptionEnum.LIST_MEMBER_IS_EMPTY)
        )

        val softId =
            KeyGenerator.generateSoftIdConversation(distinctMembers.toMutableList().apply { add(admin) })
                ?: return Mono.error(
                    ApplicationException(ExceptionEnum.LIST_MEMBER_IS_EMPTY)
                )


        return conversationRepository.findBySoftId(softId)
            .switchIfEmpty {
                userRequester.getUserInfoFriendShip(admin, users)
                    .collectList()
                    .flatMap { users ->
                        if (users.isEmpty()) return@flatMap Mono.error(ApplicationException(ExceptionEnum.LIST_MEMBER_IS_EMPTY))

                        if (users.size != distinctMembers.size) return@flatMap Mono.error(
                            ApplicationException(
                                ExceptionEnum.CONTAINS_USER_NOT_FOUND
                            )
                        )

                        val phoneNumberMembers = users.map { it.phoneNumber }.toList()
                        val conversationId = KeyGenerator.generateUuidV7()

                        val conversation =
                            Conversation().apply {
                                this.id = conversationId
                                this.isNewItem = true
                                this.softId = softId
                                this.admin = KeyGenerator.generateUuidV7()
                                this.type =
                                    if (phoneNumberMembers.size > 1) ConversationType.GROUP else ConversationType.PRIVATE
                                this.createdBy = createdBy ?: "System"
                                this.createdAt = LocalDateTime.now()
                                this.updatedAt = this.createdAt
                            }
                        return@flatMap saveConversation(conversation, admin, phoneNumberMembers)
                    }
            }
    }

    private fun saveConversation(
        conversation: Conversation,
        phoneNumberAdmin: String,
        phoneNumberMembers: List<String>
    ): Mono<Conversation> {
        val member = phoneNumberMembers.toMutableSet().apply {
            add(phoneNumberAdmin)
        }.map { phoneNumber ->
            Member().apply {
                id = if (phoneNumberAdmin == phoneNumber) conversation.admin else KeyGenerator.generateUuidV7()
                this.phoneNumber = phoneNumber
                this.role = if (phoneNumberAdmin == phoneNumber) MemberRole.ADMIN else MemberRole.USER
                this.joinedBy = conversation.admin
                this.joinedAt = conversation.createdAt
                metadata = ConversationMemberMetadata()
            }
        }

        conversation.members = member

        return conversationCacheManager.lockToCreate(conversation.softId!!, conversationRepository.save(conversation))
    }

    override fun removeConversation(id: String): Mono<Void> {
        return conversationRepository.deleteById(id)
    }

    override fun getAllDetailConversation(phoneNumber: String): Mono<List<ConversationDto>> {
        return conversationRepository.findAllDetailDtoByPhoneNumberContains(phoneNumber).collectList()
    }

    override fun existConversationById(conversationId: String): Mono<Boolean> {
        return conversationRepository.existsById(conversationId)
    }

    override fun findById(conversationId: String): Mono<Conversation> {
        return conversationRepository.findById(conversationId)
    }

    override fun getConversationInfo(conversationId: String): Mono<ConversationDto> {
        return conversationRepository.findDtoByConversationId(conversationId)
    }

    @Transactional
    override fun getConversationInfo(ownerPhone: String, users: List<String>): Mono<ConversationDto> {
        val distinctMembers = users
            .distinct()
            .toMutableList()

        if (!distinctMembers.contains(ownerPhone)) distinctMembers.add(ownerPhone)

        val softId = KeyGenerator.generateSoftIdConversation(distinctMembers) ?: return Mono.error(
            ApplicationException(
                ExceptionEnum.ERROR_GENERATE_SOFT_ID_CONVERSATION
            )
        )

        return conversationRepository.findDtoBySoftId(softId)
    }

    override fun getConversationBySoftId(softId: String): Mono<ConversationDto> {
        return conversationRepository.findDtoBySoftId(softId)
    }
}