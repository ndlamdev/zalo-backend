import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.Phonenumber
import com.lamnguyen.chat.exceptions.ApplicationException
import com.lamnguyen.chat.exceptions.ExceptionEnum

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

fun validatePhoneNumber(value: String?): Boolean {
    val phoneNumber = initPhoneNumber(value)
    return PhoneNumberUtil.getInstance().isPossibleNumberForType(phoneNumber, PhoneNumberUtil.PhoneNumberType.MOBILE)
}