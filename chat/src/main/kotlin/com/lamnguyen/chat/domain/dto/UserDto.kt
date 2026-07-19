/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 9:16 AM-05/07/2026
 *  User: ndlamdev
 **/

package com.lamnguyen.chat.domain.dto

import com.lamnguyen.chat.utils.enums.RelationShipStatus
import java.time.LocalDate

class UserDto {
    lateinit var phoneNumber: String
    var fullName: String? = null
    var birthDate: LocalDate? = null
    var avatar: String? = null
    var email: String? = null
    var displayName: String? = null
    var relationShipStatus: RelationShipStatus = RelationShipStatus.SELF
}