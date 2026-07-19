package com.lamnguyen.chat.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import java.time.LocalDateTime

open class BaseEntity : Persistable<String> {
    @Id
    private var id: String? = null

    @CreatedDate
    var createdAt: LocalDateTime? = null

    @LastModifiedDate
    var updatedAt: LocalDateTime? = null

    @CreatedBy
    var createdBy: String? = null

    @LastModifiedBy
    var updatedBy: String? = null

    var isDeleted: Boolean = false

    @Transient
    @JsonIgnore
    var isNewItem: Boolean = false

    override fun getId(): String? = id

    fun setId(id: String?) {
        this.id = id
    }

    @Transient
    @JsonIgnore
    override fun isNew(): Boolean = isNewItem
}
