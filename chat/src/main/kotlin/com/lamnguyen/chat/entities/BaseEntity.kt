package com.lamnguyen.chat.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import com.lamnguyen.chat.utils.annotations.JsonDateTimeFormat
import org.springframework.data.annotation.*
import org.springframework.data.domain.Persistable
import java.time.LocalDateTime

open class BaseEntity : Persistable<String> {
    @Id
    private var id: String? = null

    @JsonDateTimeFormat
    @CreatedDate
    var createdAt: LocalDateTime? = null

    @JsonDateTimeFormat
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
