/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 2:01 PM-08/07/2025
 *  User: kimin
 **/

package com.lamnguyen.user.models

import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime

open class BaseEntity() {
    @CreatedDate
    var createdAt: LocalDateTime? = null

    @LastModifiedDate
    var updatedAt: LocalDateTime? = null

    @CreatedBy
    var createdBy: String? = null

    @LastModifiedBy
    var updatedBy: String? = null
    var locked: Boolean = false
    var deleted: Boolean = false
}