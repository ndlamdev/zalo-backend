/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 2:14 PM-08/07/2025
 *  User: kimin
 **/

package com.lamnguyen.auth.model

import org.springframework.data.relational.core.mapping.Table


@Table(name = "roles_of_user")
data class RoleOfUser(
    val fakeId: String = "",
    var userPhoneNumber: String,
    var roleName: String
)