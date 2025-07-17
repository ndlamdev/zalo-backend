import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.Phonenumber

/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 5:16 PM-12/07/2025
 *  User: kimin
 **/

fun initPhoneNumber(value: String?): Phonenumber.PhoneNumber? {
    if (value == null) return null
    val data = value.replace(Regex("[*?_\\-.,\\s]+"), "").split("/")
    if (data.size != 2) return null
    return try {
        Phonenumber.PhoneNumber().apply {
            countryCode = data[0].toInt()
            nationalNumber = data[1].toLong()
        }
    } catch (e: NumberFormatException) {
        null
    }
}

fun formatPhoneNumber(value: String?): String? {
    val phoneNumber = initPhoneNumber(value)
    if (phoneNumber == null) return null
    return PhoneNumberUtil.getInstance().format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.E164)
}

fun validatePhoneNumber(value: String?): Boolean {
    val phoneNumber = initPhoneNumber(value)
    if (phoneNumber == null) return false
    return PhoneNumberUtil.getInstance().isPossibleNumberForType(phoneNumber, PhoneNumberUtil.PhoneNumberType.MOBILE)
}