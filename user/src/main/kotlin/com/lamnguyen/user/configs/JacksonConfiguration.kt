/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 1:51 PM-20/06/2026
 *  User: ndlamdev
 **/

package com.lamnguyen.user.configs

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import java.time.format.DateTimeFormatter


@Configuration
class JacksonConfiguration {
    @Bean
    fun jackson2ObjectMapperBuilderCustomizer(): Jackson2ObjectMapperBuilderCustomizer {
        return Jackson2ObjectMapperBuilderCustomizer { builder: Jackson2ObjectMapperBuilder? ->

            // formatter
            val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            val dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")

            // deserializers
            builder!!.deserializers(LocalDateDeserializer(dateFormatter))
            builder.deserializers(LocalDateTimeDeserializer(dateTimeFormatter))

            // serializers
            builder.serializers(LocalDateSerializer(dateFormatter))
            builder.serializers(LocalDateTimeSerializer(dateTimeFormatter))
        }
    }
}