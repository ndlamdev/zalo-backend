package com.lamnguyen.auth.utils.annotations

import com.lamnguyen.auth.utils.annotations.validators.PhoneNumberValidator
import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
@Constraint(validatedBy = [PhoneNumberValidator::class])
annotation class ValidPhoneNumber(
    val message: String = "Phone number invalid",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
