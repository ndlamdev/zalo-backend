/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 11:51 AM - 07/04/2025
 * User: kimin
 **/

package com.lamnguyen.auth.domain.dto

class ApiResponseError<T : Any> : ApiResponse<T>() {
    var error: String? = null
    var detail: T? = null
    lateinit var trace: Any
}
