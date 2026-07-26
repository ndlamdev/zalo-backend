/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 11:15 AM-19/07/2026
 *  User: ndlamdev
 **/

package com.ndlamdev.chatwsrouter.utils.properties

import com.ndlamdev.chatwsrouter.domain.dto.RSocketMetadata
import com.ndlamdev.chatwsrouter.utils.enums.RSocketServerName
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "application.rsocket")
class RSocketClientsProperties {
    val clients: Map<RSocketServerName, RSocketMetadata> = mutableMapOf()
}