package com.lamnguyen.auth.service.redis.v1

import com.lamnguyen.auth.utils.redis.ACacheRedis
import org.redisson.api.RedissonReactiveClient
import org.springframework.data.redis.core.ReactiveStringRedisTemplate
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.time.Duration
import java.time.temporal.ChronoUnit


@Service
class OtpCacheManager(
    redissonClient: RedissonReactiveClient,
    redisTemple: ReactiveStringRedisTemplate,
) : ACacheRedis<String>(redissonClient, redisTemple) {
    fun startSessionValidateAccount(phoneNumber: String): Mono<String> {
        return super.cacheData(getKeyStartSessionValidateAccount(phoneNumber), { Mono.just("1") }, 5, ChronoUnit.MINUTES)
    }

    fun saveOtp(phoneNumber: String, otp: String): Mono<String> {
        return super.cacheData(getKeySaveOtp(phoneNumber), { Mono.just(otp) }, 1, ChronoUnit.MINUTES)
    }

    fun isWaitingValidateAccount(phoneNumber: String): Mono<Boolean> {
        return super.getData(getKeyStartSessionValidateAccount(phoneNumber), false, null, null)
            .flatMap { Mono.just(true) }
    }

    fun isSentOtp(phoneNumber: String): Mono<Boolean> {
        return super.getData(getKeySaveOtp(phoneNumber), false, null, null)
            .flatMap { Mono.just(true) }
            .switchIfEmpty(Mono.just(false))
    }

    fun resetTimeRegisterSuccess(phoneNumber: String): Mono<Boolean> {
        return this.redisTemple.expire(getKeyStartSessionValidateAccount(phoneNumber), Duration.ofMinutes(5))
    }

    fun getOtp(phoneNumber: String): Mono<String> {
        return super.getData(getKeySaveOtp(phoneNumber), false, null, null);
    }

    fun clearSessionValidateAccount(phoneNumber: String): Mono<Void> {
        return super.clearCache(getKeyStartSessionValidateAccount(phoneNumber))
            .flatMap { super.clearCache(getKeySaveOtp(phoneNumber)) }
            .then()
    }

    private fun getKeyStartSessionValidateAccount(phoneNumber: String): String {
        return "VALIDATE_ACCOUNT:$phoneNumber:STATUS";
    }

    private fun getKeySaveOtp(phoneNumber: String): String {
        return "VALIDATE_ACCOUNT:$phoneNumber:OTP";
    }
}