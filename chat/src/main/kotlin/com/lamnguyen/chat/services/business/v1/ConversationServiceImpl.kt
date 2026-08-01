/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 5:17 PM-28/07/2025
 *  User: kimin
 **/

package com.lamnguyen.chat.services.business.v1

import com.lamnguyen.chat.domain.dto.ConversationDto
import com.lamnguyen.chat.domain.dto.ConversationMemberRowDto
import com.lamnguyen.chat.domain.requests.CreateConversationRequest
import com.lamnguyen.chat.entities.Conversation
import com.lamnguyen.chat.exceptions.ApplicationException
import com.lamnguyen.chat.exceptions.ExceptionEnum
import com.lamnguyen.chat.mappers.IConversationMapper
import com.lamnguyen.chat.mappers.IConversationMemberMetadataMapper
import com.lamnguyen.chat.mappers.IMemberMapper
import com.lamnguyen.chat.repositories.IConversationRepository
import com.lamnguyen.chat.services.business.IConversationMemberMetadataService
import com.lamnguyen.chat.services.business.IConversationService
import com.lamnguyen.chat.services.business.IMemberService
import com.lamnguyen.chat.services.business.IMessageService
import com.lamnguyen.chat.services.rsocket.IUserRequester
import com.lamnguyen.chat.utils.enums.ConversationType
import com.lamnguyen.chat.utils.helpers.KeyGenerator
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import reactor.kotlin.core.util.function.component1
import reactor.kotlin.core.util.function.component2

@Service
class ConversationServiceImpl(
    private val conversationRepository: IConversationRepository,
    private val userRequester: IUserRequester,
    private val memberService: IMemberService,
    private val cmmService: IConversationMemberMetadataService,
    private val conversationMapper: IConversationMapper,
    private val memberMapper: IMemberMapper,
    private val conversationMemberMetadataMapper: IConversationMemberMetadataMapper,
    private val messageService: IMessageService
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
    override fun createConversation(admin: String, users: List<String>): Mono<Conversation> {
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
        return conversationRepository.save(conversation)
            .flatMap {
                Mono.zip(
                    memberService.addMembersRoleUser(it.id!!, conversation.admin!!, phoneNumberMembers)
                        .collectList(),
                    memberService.addMemberRoleAdmin(it.id!!, conversation.admin!!, phoneNumberAdmin)
                )
                    .flatMap { (users, admin) ->
                        users.add(admin)
                        val members = users.asSequence().map { member -> member.id!! }.toList()

                        return@flatMap cmmService.addMembers(it.id!!, members).collectList()
                            .thenReturn(it)
                    }
            }
    }

    override fun removeConversation(id: String): Mono<Void> {
        return conversationRepository.deleteById(id)
    }

    override fun getAllConversation(phoneNumber: String): Mono<List<ConversationDto>> {
        val conversationRows = conversationRepository.findAllDtoByPhoneNumberContains(phoneNumber)
        return collectHashMapConversation(conversationRows)
            .flatMap { conversations ->
                val ids = conversations.keys.toList()

                messageService.getLastMessageAndPinMessages(ids)
                    .doOnNext { message ->
                        val conversation = conversations[message.conversationId!!] ?: return@doOnNext
                        if (message.isPinned)
                            conversation.pinMessages.add(message)
                        else conversation.lastMessage = message
                    }
                    .collectList().thenReturn(conversations.values.toList())
            }
    }

    private fun collectHashMapConversation(rows: Flux<ConversationMemberRowDto>): Mono<HashMap<String, ConversationDto>> {
        return rows.collect(
            { LinkedHashMap<String, ConversationDto>() },
            { conversations, row ->
                val conversation = conversations.getOrPut(row.id) {
                    conversationMapper.toDto(row)
                }

                conversation.members.add(
                    memberMapper.toEntity(row).apply {
                        metadata = conversationMemberMetadataMapper.toEntity(row)
                    }
                )
            }
        )
    }

    private fun collectListConversation(rows: Flux<ConversationMemberRowDto>): Mono<List<ConversationDto>> {
        return collectHashMapConversation(rows).map { it.values.toList() }
    }

    override fun existConversationById(conversationId: String): Mono<Boolean> {
        return conversationRepository.existsById(conversationId)
    }

    override fun findById(conversationId: String): Mono<Conversation> {
        return conversationRepository.findById(conversationId)
    }

    override fun getConversationInfo(conversationId: String): Mono<ConversationDto> {
        val rows = conversationRepository.findDtoByConversationId(conversationId)
        return collectListConversation(rows).map { it.first() }
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
        val rows = conversationRepository.findDtoBySoftId(softId)
            .switchIfEmpty(
                createConversation(ownerPhone, users)
                    .flatMapMany {
                        conversationRepository.findDtoBySoftId(softId)
                    })
        return collectListConversation(rows).map { it.first() }
    }

    override fun getConversationBySoftId(softId: String): Mono<ConversationDto> {
        val rows = conversationRepository.findDtoBySoftId(softId)
        return collectListConversation(rows).map { it.first() }
    }
}