/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 4:49 PM-14/07/2025
 *  User: kimin
 **/

package com.lamnguyen.user.models

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate

@Table("users")
open class User : BaseEntity(), Persistable<String> {
    @Id
    lateinit var phoneNumber: String
    var fullName: String? = null
    var birthDate: LocalDate? = null
    var avatar: String? = null
    var email: String? = null

    @Transient
    var isNewUser: Boolean = false

    override fun getId(): String? = phoneNumber

    override fun isNew(): Boolean = isNewUser
}