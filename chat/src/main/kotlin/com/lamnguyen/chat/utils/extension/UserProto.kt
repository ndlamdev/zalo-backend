/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 3:42 PM-30/07/2025
 *  User: kimin
 **/

package com.lamnguyen.chat.utils.extension

import com.lamnguyen.chat.protos.FriendShipCheckResult

fun FriendShipCheckResult.Builder.initWithYourSelf(phoneNumber: String): FriendShipCheckResult {
    return this.apply {
        phoneNumberChecker = phoneNumber
        phoneNumberFriend = phoneNumber
        result = true
    }
        .build()
}