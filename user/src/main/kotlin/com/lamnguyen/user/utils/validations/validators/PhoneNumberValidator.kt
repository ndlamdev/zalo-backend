package com.lamnguyen.user.utils.validations.validators

import com.lamnguyen.user.utils.helpers.validatePhoneNumber
import com.lamnguyen.user.utils.validations.ValidPhoneNumber
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 5:19 PM-12/07/2025
 *  User: kimin
 **/
class PhoneNumberValidator : ConstraintValidator<ValidPhoneNumber, String> {
    override fun isValid(
        value: String?,
        context: ConstraintValidatorContext?
    ): Boolean {
        return validatePhoneNumber(value)
    }
}