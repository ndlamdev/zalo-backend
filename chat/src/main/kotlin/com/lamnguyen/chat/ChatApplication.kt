package com.lamnguyen.chat

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
//import org.springframework.grpc.client.ImportGrpcClients
//
//@ImportGrpcClients(basePackageClasses = [ChatApplication::class])
@SpringBootApplication
class ChatApplication

fun main(args: Array<String>) {
    runApplication<ChatApplication>(*args)
}
