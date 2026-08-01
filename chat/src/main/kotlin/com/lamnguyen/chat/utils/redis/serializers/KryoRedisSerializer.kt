package com.lamnguyen.chat.utils.redis.serializers

import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.SerializerFactory
import com.esotericsoftware.kryo.io.Input
import com.esotericsoftware.kryo.io.Output
import com.esotericsoftware.kryo.serializers.CompatibleFieldSerializer.CompatibleFieldSerializerConfig
import org.springframework.data.redis.serializer.RedisSerializer
import org.springframework.data.redis.serializer.SerializationException
import java.io.ByteArrayInputStream

class KryoRedisSerializer<T>(
    private val type: Class<T>,
    private vararg val otherType: Class<*>
) :
    RedisSerializer<T?> {
    private val initialBufferSize = 4096

    private val threadLocalKryo = ThreadLocal.withInitial {
        val config =
            CompatibleFieldSerializerConfig()

        config.setChunkedEncoding(true)
        config.setReadUnknownFieldData(true)
        config.setExtendedFieldNames(false)

        Kryo().apply {
            isRegistrationRequired = true
            references = false
            setDefaultSerializer(SerializerFactory.CompatibleFieldSerializerFactory(config))
            register(type)
            otherType.forEach { register(it) }
        }
    }

    @Throws(SerializationException::class)
    override fun serialize(value: T?): ByteArray? {
        if (value == null) {
            return null
        }
        val kryo = threadLocalKryo.get()
        try {
            Output(initialBufferSize, -1).use { output ->
                kryo.writeClassAndObject(output, value)
                return output.toBytes()
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
                    @Suppress("UNCHECKED_CAST")
                    return kryo.readClassAndObject(input) as T?
                }
            }
        } catch (e: Exception) {
            throw SerializationException("Error deserializing object with Kryo", e)
        }
    }
}