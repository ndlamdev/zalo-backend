/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 2:01 PM-08/07/2025
 *  User: kimin
 **/

package com.lamnguyen.chat.entities

import org.springframework.data.annotation.*
import java.time.LocalDateTime

open class BaseEntity() {
    @Id
    var id: Long? = null

    @CreatedDate
    lateinit var createdAt: LocalDateTime

    @LastModifiedDate
    lateinit var updatedAt: LocalDateTime

    @CreatedBy
    var createdBy: String? = null

    @LastModifiedBy
    var updatedBy: String? = null
    var locked: Boolean = false
    var deleted: Boolean = false
}