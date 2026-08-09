package com.lamnguyen.chat.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import com.lamnguyen.chat.utils.annotations.JsonDateTimeFormat
import org.springframework.data.annotation.*
import org.springframework.data.domain.Persistable
import org.springframework.data.mongodb.core.mapping.Field
import org.springframework.data.mongodb.core.mapping.MongoId
import java.time.LocalDateTime

open class BaseEntity : Persistable<String> {
    @MongoId
    private var id: String? = null

    @JsonDateTimeFormat
    @CreatedDate
    @Field("created_at")
    var createdAt: LocalDateTime? = null

    @JsonDateTimeFormat
    @LastModifiedDate
    @Field("updated_at")
    var updatedAt: LocalDateTime? = null

    @CreatedBy
    @Field("created_by")
    var createdBy: String? = null

    @LastModifiedBy
    @Field("updated_by")
    var updatedBy: String? = null

    @Field("is_deleted")
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
