/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 2:41 PM-14/06/2026
 *  User: ndlamdev
 **/

package com.lamnguyen.auth.model

import org.springframework.data.relational.core.mapping.Table

@Table(name = "roles_of_user")
class RolesOfUser {
    lateinit var roleName: String
    lateinit var userPhoneNumber: String
}