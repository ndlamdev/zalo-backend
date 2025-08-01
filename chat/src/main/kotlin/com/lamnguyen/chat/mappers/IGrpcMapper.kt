/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 12:09 PM-05/05/2025
 * User: kimin
 */
package com.lamnguyen.chat.mappers

import com.google.protobuf.Int64Value
import com.google.protobuf.StringValue
import com.google.protobuf.Timestamp
import org.mapstruct.Mapper
import java.time.*

@Mapper(componentModel = "spring")
interface IGrpcMapper {
    fun toString(stringValue: StringValue): String? {
        if (StringValue.getDefaultInstance() == stringValue) return null
        return stringValue.getValue()
    }

    fun toStringValue(value: String?): StringValue? {
        if (value == null) return StringValue.getDefaultInstance()
        return StringValue.newBuilder().setValue(value).build()
    }

    fun formatDateTime(dateTime: LocalDateTime?): Timestamp? {
        if (dateTime == null) return Timestamp.getDefaultInstance()

        val instant = dateTime.atZone(zoneId).toInstant()

        return Timestamp
            .newBuilder()
            .setSeconds(instant.epochSecond)
            .setNanos(instant.nano)
            .build()
    }

    fun formatDateTime(timestamp: Timestamp): LocalDateTime? {
        if (Timestamp.getDefaultInstance() == timestamp) return null

        return LocalDateTime
            .ofEpochSecond(timestamp.seconds, timestamp.nanos, zoneOffset)
    }

    fun formatDate(localDate: LocalDate?): Timestamp? {
        if (localDate == null) return Timestamp.getDefaultInstance()
        val instant = localDate.atStartOfDay(zoneId).toInstant()
        return Timestamp.newBuilder()
            .setSeconds(instant.epochSecond)
            .setNanos(instant.nano)
            .build()
    }

    fun formatDate(timestamp: Timestamp): LocalDate? {
        if (Timestamp.getDefaultInstance() == timestamp) return null
        val instant = Instant.ofEpochSecond(timestamp.seconds, timestamp.nanos.toLong())
        return LocalDate.ofInstant(instant, zoneId)
    }

    fun toInt64Value(value: Long?): Int64Value? {
        if (value == null) return Int64Value.getDefaultInstance()
        return Int64Value.newBuilder().setValue(value).build()
    }

    fun toLongValue(value: Int64Value?): Long? {
        if (value == null) return null
        return value.value
    }

    companion object {
        val zoneId: ZoneId = ZoneId.of("Asia/Ho_Chi_Minh")
        val zoneOffset: ZoneOffset = ZoneOffset.of("+07:00")
    }
}
