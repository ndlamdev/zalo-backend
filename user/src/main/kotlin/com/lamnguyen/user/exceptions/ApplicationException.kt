/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 6:39 AM-10/07/2025
 *  User: kimin
 **/

package com.lamnguyen.user.exceptions

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
class ApplicationException(enum: ExceptionEnum, val detail: Any? = null) : Throwable(enum.message) {
    var code: Int = enum.code
}