/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 10:40 AM-24/08/2025
 *  User: kimin
 **/

package com.lamnguyen.chatws.domain.requests

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.nimbusds.openid.connect.sdk.assurance.evidences.attachment.Attachment

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
open class Message {
    var roomChatId: String? = null
    var content: String? = null
    var users: List<String> = emptyList()
    var attachments: List<Any> = emptyList()
}