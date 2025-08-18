/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 2:09 PM-08/08/2025
 *  User: kimin
 **/

package com.lamnguyen.user.domain.dto

import com.lamnguyen.user.models.User

class UserDto : User() {
    var displayName: String? = null
    var isFriend: Boolean = false
    var addFriendRequested: Boolean = false
}