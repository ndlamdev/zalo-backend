/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 6:41 PM-12/07/2025
 *  User: kimin
 **/

package com.lamnguyen.auth.utils.annotations.validators

import com.lamnguyen.auth.utils.annotations.PasswordMatches
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class PasswordMatchesValidator : ConstraintValidator<PasswordMatches, Any> {

    override fun isValid(
        obj: Any?,
        context: ConstraintValidatorContext?
    ): Boolean {
        if (obj == null) return false

        try {
            val passwordField = obj::class.members.firstOrNull { it.name == "password" } ?: return false
            val confirmPasswordField = obj::class.members.firstOrNull { it.name == "confirmPassword" } ?: return false

            val password = passwordField.call(obj) as? String
            val confirmPassword = confirmPasswordField.call(obj) as? String

            return password != null && password == confirmPassword
        } catch (e: Exception) {
            return false
        }
    }
}