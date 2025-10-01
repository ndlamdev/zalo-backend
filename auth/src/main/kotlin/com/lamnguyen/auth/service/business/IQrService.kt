/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 5:15 PM-30/09/2025
 *  User: kimin
 **/

package com.lamnguyen.auth.service.business

import com.lamnguyen.auth.domain.dto.RefreshTokenPayload
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface IQrService {
    fun createQrCodeLoginAndToken(): Mono<Map<String, String>>

    fun subscribe(token: String): Flux<String>

    fun confirm(tokenQrCode: String, refreshToken: String): Mono<Void>

    fun checkRefreshToken(refreshToken: String): Mono<RefreshTokenPayload>
}