/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 12:44 PM-24/07/2025
 *  User: kimin
 **/

package com.lamnguyen.user.models

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table(name = "invite_add_friends")
class InviteAddFriend : BaseEntity() {
    @Id
    var id: Long? = null
    lateinit var phoneNumberSender: String
    lateinit var phoneNumberReceiver: String
    var message: String? = null
    var accepted: Boolean = false
}