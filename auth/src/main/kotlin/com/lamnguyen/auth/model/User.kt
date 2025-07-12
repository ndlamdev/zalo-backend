/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 2:00 PM-08/07/2025
 *  User: kimin
 **/

package com.lamnguyen.auth.model

import org.springframework.data.relational.core.mapping.Table


@Table(name = "users")
class User : BaseEntity() {
    lateinit var phoneNumber: String
    lateinit var password: String
    lateinit var email: String
}