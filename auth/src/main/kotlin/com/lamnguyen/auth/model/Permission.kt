/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 2:23 PM-08/07/2025
 *  User: kimin
 **/

package com.lamnguyen.auth.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table(name = "permissions")
class Permission : BaseEntity() {
    @Id
    lateinit var name: String
}