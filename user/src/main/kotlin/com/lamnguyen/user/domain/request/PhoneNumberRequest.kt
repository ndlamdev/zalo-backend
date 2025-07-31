/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 7:38 PM-15/07/2025
 *  User: kimin
 **/

package com.lamnguyen.user.domain.request

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.lamnguyen.user.utils.validations.ValidPhoneNumber

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
class PhoneNumberRequest(@ValidPhoneNumber var phoneNumber: String)