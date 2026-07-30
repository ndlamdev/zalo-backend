package com.ndlamdev.chatwsrouter.utils.annotation

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer

@Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
@JsonSerialize(using = LocalDateTimeSerializer::class)
@JsonDeserialize(using = LocalDateTimeDeserializer::class)
@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
annotation class JsonDateTimeFormat
