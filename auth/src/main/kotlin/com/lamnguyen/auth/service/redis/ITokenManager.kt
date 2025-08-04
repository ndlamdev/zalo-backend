/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 5:03 PM-02/08/2025
 *  User: kimin
 **/

package com.lamnguyen.auth.service.redis

import reactor.core.publisher.Mono

interface ITokenManager {
    fun existsTokenInBlackList(tokenId: String): Mono<Boolean>
    fun saveTokenInBlackList(tokenId: String): Mono<String>
}