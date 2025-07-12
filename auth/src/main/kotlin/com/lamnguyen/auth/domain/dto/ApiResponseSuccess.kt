/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 11:51 AM - 07/04/2025
 * User: kimin
 **/

package com.lamnguyen.auth.domain.dto

class ApiResponseSuccess<T : Any> : ApiResponse<T>() {
    lateinit var message: String
    lateinit var data: T
}
