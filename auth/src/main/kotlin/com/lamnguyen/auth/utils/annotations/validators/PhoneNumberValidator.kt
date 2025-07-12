/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 5:19 PM-12/07/2025
 *  User: kimin
 **/

package com.lamnguyen.auth.utils.annotations.validators

import com.lamnguyen.auth.utils.annotations.ValidPhoneNumber
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import validatePhoneNumber

class PhoneNumberValidator : ConstraintValidator<ValidPhoneNumber, String> {
    override fun isValid(
        value: String?,
        context: ConstraintValidatorContext?
    ): Boolean {
        return validatePhoneNumber(value)
    }
}