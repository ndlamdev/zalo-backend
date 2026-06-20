package com.lamnguyen.user.utils.helpers

import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.Phonenumber
import com.lamnguyen.user.exceptions.ApplicationException
import com.lamnguyen.user.exceptions.ExceptionEnum

/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 5:16 PM-12/07/2025
 *  User: kimin
 **/

fun initPhoneNumber(value: String?): Phonenumber.PhoneNumber {
    return try {
        parsePhoneNumber(value)
    } catch (_: Exception) {
        val data = value!!.replace(Regex("[*?_\\-.,\\s]+"), "").split("/")
        try {
            Phonenumber.PhoneNumber().apply {
                countryCode = data[0].toInt()
                nationalNumber = data[1].toLong()
            }
        } catch (_: Exception) {
            throw ApplicationException(ExceptionEnum.ERROR_FORMAT_PHONE_NUMBER)
        }
    }
}

fun initPhoneNumber(value: String?, countryCode: Int): Phonenumber.PhoneNumber {
    return try {
        Phonenumber.PhoneNumber().apply {
            this.countryCode = countryCode
            nationalNumber = value!!.toLong()
        }
    } catch (_: Exception) {
        throw ApplicationException(ExceptionEnum.ERROR_FORMAT_PHONE_NUMBER)
    }
}

fun parsePhoneNumber(value: String?): Phonenumber.PhoneNumber {
    val phoneUtil = PhoneNumberUtil.getInstance()
    if (value == null || !value.startsWith("+"))
        throw ApplicationException(ExceptionEnum.ERROR_FORMAT_PHONE_NUMBER)
    return phoneUtil.parse(value, null)
}

fun formatPhoneNumber(value: String?): String {
    val phoneNumber = initPhoneNumber(value)
    return PhoneNumberUtil.getInstance().format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.E164)
}

fun formatPhoneNumber(value: String?, countryCode: Int): String {
    val phoneNumber = try {
        initPhoneNumber(value)
    } catch (_: Exception) {
        initPhoneNumber(value, countryCode)
    }
    return PhoneNumberUtil.getInstance().format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.E164)
}

fun formatPhoneNumberSameRegionWithOtherPhoneNumber(value: String?, otherPhoneNumber: String): String {
    val otherPhoneNumberRegion = parsePhoneNumber(otherPhoneNumber).countryCode
    val phoneNumber = initPhoneNumber(value, otherPhoneNumberRegion)
    return PhoneNumberUtil.getInstance().format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.E164)
}

/**
 * Formats a phone number using specific formatting rules. The method attempts to format the phone
 * number using various strategies in the following order:
 * 1. Standard phone number formatting.
 * 2. Formatting within the same region as another phone number.
 * 3. Default value if previous attempts fail.
 *
 * @param value The phone number to be formatted.
 * @param examplePhoneNumberFormated An example phone number for determining the region, used as a reference.
 * @param defaultValue A fallback value to return if formatting fails. Defaults to the original phone number value.
 * @return A formatted phone number string, or the provided default value if formatting is unsuccessful.
 */
fun formatPhoneNumber(value: String, examplePhoneNumberFormated: String, defaultValue: String? = value): String {
    return try {
        formatPhoneNumber(value)
    } catch (_: Exception) {
        try {
            formatPhoneNumberSameRegionWithOtherPhoneNumber(value, examplePhoneNumberFormated)
        } catch (_: Exception) {
            defaultValue ?: value
        }
    }
}

fun validatePhoneNumber(value: String?): Boolean {
    val phoneNumber = initPhoneNumber(value)
    return PhoneNumberUtil.getInstance().isPossibleNumberForType(phoneNumber, PhoneNumberUtil.PhoneNumberType.MOBILE)
}