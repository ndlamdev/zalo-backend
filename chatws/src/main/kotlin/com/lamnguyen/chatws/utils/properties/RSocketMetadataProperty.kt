package com.lamnguyen.chatws.utils.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties("spring.rsocket.server")
class RSocketMetadataProperty {
    var host: String? = null
    var port: Int = 0
    var dataMime: String? = null
}
