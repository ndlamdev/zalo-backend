package com.lamnguyen.chat.utils.credentials

import io.grpc.CallCredentials
import io.grpc.Metadata
import java.util.concurrent.Executor

class BearerTokenCallCredentials(val token: String?) : CallCredentials() {

    override fun applyRequestMetadata(
        requestInfo: RequestInfo,
        executor: Executor,
        applier: MetadataApplier
    ) {
        executor.execute {
            if (token == null)
                return@execute
            val headers = Metadata()
            val authKey = Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER)
            headers.put(authKey, "Bearer $token")
            applier.apply(headers)
        }
    }
}