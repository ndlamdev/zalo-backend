/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 6:41 AM-10/07/2025
 *  User: kimin
 **/

package com.lamnguyen.user.exceptions

enum class ExceptionEnum(val code: Int, val message: String) {
    CREATE_USER_FAILED(11, "Create User Failed"),
    UNAUTHENTICATED(12, "Unauthenticated"),
    INVITE_ADD_FRIEND_FAILED(13, "Add Friend Failed"),
    USER_NOT_FOUND(14, "User Not Found"),
    INVITE_EXISTS(15, "Invite Exists"),
}