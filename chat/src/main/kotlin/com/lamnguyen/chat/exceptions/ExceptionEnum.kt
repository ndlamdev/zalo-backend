/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 6:41 AM-10/07/2025
 *  User: kimin
 **/

package com.lamnguyen.chat.exceptions

enum class ExceptionEnum(val code: Int, val message: String) {
    ERROR_FORMAT_PHONE_NUMBER(4001, "Invalid phone number!"),
    LIST_MEMBER_IS_EMPTY(4002, "List member is empty!"),
    REQUIRED_PAYLOAD(4000, "Required Payload"),
    CONTAINS_USER_NOT_FOUND(4003, "Contains User not found!"),
    ERROR_GENERATE_SOFT_ID_CONVERSATION(4901, "Error generating soft id conversation!"),

    ;
}