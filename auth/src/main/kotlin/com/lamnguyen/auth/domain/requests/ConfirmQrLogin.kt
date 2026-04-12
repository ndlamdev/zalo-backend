/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 2:11 PM-12/04/2026
 *  User: kimin
 **/

package com.lamnguyen.auth.domain.requests

import com.lamnguyen.auth.utils.enums.LoginStatus


class ConfirmQrLogin : TokenRequest() {
    lateinit var status: LoginStatus
}