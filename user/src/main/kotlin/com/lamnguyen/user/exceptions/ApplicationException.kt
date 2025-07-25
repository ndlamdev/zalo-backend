/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 6:39 AM-10/07/2025
 *  User: kimin
 **/

package com.lamnguyen.user.exceptions

class ApplicationException(enum: ExceptionEnum) : Throwable(enum.message) {
    var code: Int = enum.code
    var detail: Any? = null

    constructor(enum: ExceptionEnum, detail: Any? = null) : this(enum) {
        this.detail = detail
    }
}