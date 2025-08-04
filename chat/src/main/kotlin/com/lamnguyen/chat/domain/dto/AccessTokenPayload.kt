/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 2:31 PM-08/07/2025
 *  User: kimin
 **/

package com.lamnguyen.chat.domain.dto

import com.fasterxml.jackson.annotation.JsonInclude

class AccessTokenPayload : SimplePayload() {
    var refreshTokenId: String? = null

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    var roles: MutableSet<String?>? = null
}