/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 9:29 AM-05/07/2026
 *  User: ndlamdev
 **/

package com.lamnguyen.chat.services.local

import com.lamnguyen.chat.utils.enums.RSocketServerName
import com.lamnguyen.chat.utils.properties.RSocketClientsProperties
import io.rsocket.core.RSocketConnector
import jakarta.annotation.PostConstruct
import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.stereotype.Service
import org.springframework.util.MimeTypeUtils
import reactor.util.retry.Retry
import java.time.Duration

@Service

class RSocketRequesterManager(
    private val requesterBuilder: RSocketRequester.Builder,
    private val properties: RSocketClientsProperties
) {
    private val clients: MutableMap<RSocketServerName, RSocketRequester> = mutableMapOf()

    @PostConstruct
    fun initRequester() {
        properties.clients.forEach { (key, value) ->
            val requester = requesterBuilder
                .rsocketConnector { rSocketConnector: RSocketConnector? ->
                    rSocketConnector!!.reconnect(
                        Retry.fixedDelay(2, Duration.ofSeconds(2))
                    )
                }
                .dataMimeType(MimeTypeUtils.parseMimeType(value.dataMime))
                .tcp(value.host, value.port)

            clients[key] = requester
        }
    }

    fun get(serviceId: RSocketServerName): RSocketRequester? {
        return clients[serviceId]
    }
}
