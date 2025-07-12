package com.lamnguyen.auth.utils.annotations

import com.lamnguyen.auth.utils.annotations.validators.PasswordMatchesValidator
import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@MustBeDocumented
@Constraint(validatedBy = [PasswordMatchesValidator::class])
@Target(AnnotationTarget.CLASS) // ⚠️ Quan trọng: áp dụng cho class, không phải field
@Retention(AnnotationRetention.RUNTIME)
annotation class PasswordMatches(
    val message: String = "Password and Confirm password not match",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)