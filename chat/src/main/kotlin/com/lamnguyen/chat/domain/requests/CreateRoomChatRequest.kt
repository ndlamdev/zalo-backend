/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 5:10 PM-28/07/2025
 *  User: kimin
 **/

package com.lamnguyen.chat.domain.requests

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import org.springframework.data.annotation.Transient


class CreateRoomChatRequest {
    @NotNull
    @NotEmpty
    var members: MutableList<String>? = null

    @NotNull
    @NotBlank
    var title: String? = null

    @Transient
    var adminRoomChat: String? = null
}