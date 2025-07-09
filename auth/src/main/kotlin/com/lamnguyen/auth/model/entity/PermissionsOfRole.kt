/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 2:24 PM-08/07/2025
 *  User: kimin
 **/

package com.lamnguyen.auth.model.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table(name = "permissions_of_role")
data class PermissionsOfRole(
    @Id
    val fakeId: String = "",
    var permissionName: String,
    var roleName: String,
)