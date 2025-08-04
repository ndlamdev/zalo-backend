/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 6:41 AM-10/07/2025
 *  User: kimin
 **/

package com.lamnguyen.chat.exceptions

enum class ExceptionEnum(val code: Int, val message: String) {
    ERROR_FORMAT_PHONE_NUMBER(4001, "User already exists!"),
    LIST_MEMBER_IS_EMPTY(4002, "List member is empty!"),
}