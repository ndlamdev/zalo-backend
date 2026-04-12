/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 5:15 PM-30/09/2025
 *  User: kimin
 **/

package com.lamnguyen.auth.service.business

import com.lamnguyen.auth.domain.responses.TokenResponse
import com.lamnguyen.auth.utils.enums.LoginStatus
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface IQrService {
    fun createQrCodeLoginAndToken(): Mono<Map<String, String>>

    fun subscribe(qrScanToken: String): Flux<String>

    fun confirm(qrScanToken: String, status: LoginStatus): Mono<Void>

    fun scan(qrScanToken: String): Mono<Void>

    fun login(loginToken: String?): Mono<TokenResponse>
}