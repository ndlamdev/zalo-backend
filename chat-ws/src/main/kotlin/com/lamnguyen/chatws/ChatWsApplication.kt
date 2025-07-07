package com.lamnguyen.chatws

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ChatWsApplication

fun main(args: Array<String>) {
    runApplication<ChatWsApplication>(*args)
}
