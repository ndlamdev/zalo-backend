/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 5:13 PM-28/07/2025
 *  User: kimin
 **/

package com.lamnguyen.chat.repositories

import com.lamnguyen.chat.domain.dto.ConversationDto
import com.lamnguyen.chat.entities.Conversation
import org.springframework.data.mongodb.repository.Aggregation
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface IConversationRepository : ReactiveCrudRepository<Conversation, String> {
    @Aggregation(
        pipeline = [
            """
            {
                ${"$"}match: {
                    'members.phone_number': ?0
                }
            }
            """,

            """
            {
                ${"$"}set: {
                    'viewer': {
                        ${"$"}arrayElemAt: [
                            {
                                ${"$"}filter: {
                                    input: '${"$"}members',
                                    as: 'member',
                                    cond: {
                                        ${"$"}eq: [
                                            '${"$$"}member.phone_number',
                                            ?0
                                        ]
                                    }
                                }
                            },
                            0
                        ]
                    }
                }
            }
            """,

            """
            {
                ${"$"}lookup: {
                    from: 'message',
                    let: {
                        'deleted_conversation_at':
                            '${"$"}viewer.metadata.deleted_conversation_at'
                    },
                    localField: "_id",
                    foreignField: "conversation_id",
                    pipeline: [
                        {
                            ${"$"}match: {
                                ${"$"}expr: {
                                    ${"$"}gte: [
                                        '${"$"}created_at',
                                        '${"$$"}deleted_conversation_at'
                                    ]
                                }
                            }
                        },
                        {
                            ${"$"}sort: {
                                'created_at': 1
                            }
                        }
                    ],
                    as: 'messages'
                }
            }
            """,

            """
            {
                ${"$"}set: {
                    'messages': {
                        ${"$"}map: {
                            input: '${"$"}messages',
                            as: 'message',
                            in: {
                                ${"$"}mergeObjects: [
                                    '${"$$"}message',
                                    {
                                        'status': {
                                            ${"$"}arrayElemAt: [
                                                {
                                                    ${"$"}filter: {
                                                        input:
                                                            '${"$$"}message.statuses',
                                                        as: 'status',
                                                        cond: {
                                                            ${"$"}eq: [
                                                                '${"$$"}status.member_id',
                                                                '${"$"}viewer._id'
                                                            ]
                                                        }
                                                    }
                                                },
                                                0
                                            ]
                                        }
                                    }
                                ]
                            }
                        }
                    }
                }
            }
            """,

            """
            {
                ${"$"}set: {
                    'pinned':
                        '${"$"}viewer.metadata.is_pinned',

                    'muted':
                        '${"$"}viewer.metadata.is_muted',

                    'archived':
                        '${"$"}viewer.metadata.is_archived',

                    'last_message': {
                        ${"$"}arrayElemAt: [
                            '${"$"}messages',
                            -1
                        ]
                    },

                    'total_message_unread': {
                        ${"$"}sum: {
                            ${"$"}map: {
                                input: '${"$"}messages',
                                as: 'message',
                                in: {
                                    ${"$"}cond: [
                                        {
                                            ${"$"}eq: [
                                                '${"$$"}message.status.status',
                                                'RECEIVED'
                                            ]
                                        },
                                        1,
                                        0
                                    ]
                                }
                            }
                        }
                    }
                }
            }
            """,

            """
            {
                ${"$"}unset: [
                    'viewer',
                    'messages',
                    'last_message.statuses',
                    'members.metadata'
                ]
            }
            """
        ]
    )
    fun findAllDetailDtoByPhoneNumberContains(phoneNumber: String): Flux<ConversationDto>

    @Aggregation(
        pipeline = [
            """
            {
            ${"$"}match: {
                "_id": ?0
                }
            }
        """,
            """
            {
                ${"$"}unset: [  "members.metadata"  ]
            }
        """
        ]
    )
    fun findDtoByConversationId(conversationId: String): Mono<ConversationDto>

    @Aggregation(
        pipeline = [
            """
            {
            ${"$"}match: {
                "soft_id": ?0
                }
            }
        """,
            """
            {
                ${"$"}unset: [  "members.metadata"  ]
            }
        """
        ]
    )
    fun findDtoBySoftId(softId: String): Mono<ConversationDto>

    fun findBySoftId(softId: String): Mono<Conversation>
    fun countBySoftId(softId: String): Mono<Int>
}