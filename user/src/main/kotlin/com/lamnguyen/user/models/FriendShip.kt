/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 12:31 PM-24/07/2025
 *  User: kimin
 **/

package com.lamnguyen.user.models

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("friendships")
class FriendShip : BaseEntity() {
    @Id
    var id: Long? = null
    var ownerPhoneNumber: String = ""
    var friendPhoneNumber: String = ""
    var displayName: String? = null
}