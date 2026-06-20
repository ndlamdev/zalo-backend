/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 6:20 AM-21/06/2026
 *  User: ndlamdev
 **/

package com.lamnguyen.user.utils.helpers

import org.springframework.web.reactive.function.server.ServerRequest

fun ServerRequest.queryParamWithDefaultValue(name: String, defaultValue: String): String {
    return this.queryParam(name).orElse(defaultValue) ?: defaultValue
}