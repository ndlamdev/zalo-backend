/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 6:41 AM-10/07/2025
 *  User: kimin
 **/

package com.lamnguyen.auth.exceptions

enum class ExceptionEnum(val code: Int, val message: String) {
    REGISTER_ERROR(1001, "Register error!"),
    USER_EXISTED(1002, "User already exists!"),
    ERROR_FORMAT_PHONE_NUMBER(1003, "Invalid phone number!"),
    USER_NOT_EXISTS(1004, "User not exists!"),
    LOGIN_FAILED(1005, "Login failed!"),
    RESIGN_FAILED(1006, "Resign failed!"),
    MISSING_ACCESS_TOKEN(1007, "Access token missing!"),
    MISSING_REFRESH_TOKEN(1008, "Refresh token missing!"),
    EMPTY_DATA(1009, "Empty data!"),
    WRONG_TOKEN_EXPIRED(1010, "Token issued before password change"),
    BLACKLIST_TOKEN(1011, "Token has been blacklisted"),
    SID_EXPIRED(1012, "Sid was expired!"),
    INVALID_LOGIN_STATUS(1013, "Invalid login status!"),
    INVALID_TOKEN(1013, "Invalid token!"),
    OTP_SEND(1014, "OTP sent successfully!"),
    INVALID_PHONE_NUMBER(1015, "Invalid phone number!"),
    INVALID_OTP(1016, "Invalid otp!"),
    OTP_EXPIRED(1017, "OTP was expired!"),
}