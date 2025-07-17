/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 7:38 PM-15/07/2025
 *  User: kimin
 **/

package com.lamnguyen.user.domain.request

import com.lamnguyen.user.utils.validations.ValidPhoneNumber

class PhoneNumberRequest {
    @ValidPhoneNumber
    lateinit var phoneNumber: String
}