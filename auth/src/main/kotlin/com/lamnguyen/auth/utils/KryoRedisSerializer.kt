package com.lamnguyen.auth.utils

import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.io.Input
import com.esotericsoftware.kryo.io.Output
import org.springframework.data.redis.serializer.RedisSerializer
import org.springframework.data.redis.serializer.SerializationException
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class KryoRedisSerializer<T>(
    private val type: Class<T>,
    private vararg val otherType: Class<*>
) :
    RedisSerializer<T?> {
    private val threadLocalKryo = ThreadLocal.withInitial {
        Kryo().apply {
            isRegistrationRequired = false
            register(type)
            otherType.forEach { register(it) }
        }
    }

    @Throws(SerializationException::class)
    override fun serialize(obj: T?): ByteArray? {
        if (obj == null) {
            return null
        }
        val kryo = threadLocalKryo.get()
        try {
            ByteArrayOutputStream().use { bos ->
                Output(bos).use { output ->
                    kryo.writeObject(output, obj)
                    return bos.toByteArray()
                }
            }
        } catch (e: Exception) {
            throw SerializationException("Error serializing object with Kryo", e)
        }
    }

    @Throws(SerializationException::class)
    override fun deserialize(bytes: ByteArray?): T? {
        if (bytes == null || bytes.isEmpty()) {
            return null
        }
        val kryo = threadLocalKryo.get()
        try {
            ByteArrayInputStream(bytes).use { bis ->
                Input(bis).use { input ->
                    return kryo.readObject<T?>(input, type)
                }
            }
        } catch (e: Exception) {
            throw SerializationException("Error deserializing object with Kryo", e)
        }
    }
}