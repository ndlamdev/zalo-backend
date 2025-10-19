/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 4:56 PM-28/07/2025
 *  User: kimin
 **/

package com.lamnguyen.chat.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Transient
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Table

@Table("room_chats")
class RoomChat : BaseEntity(), Persistable<String> {
    @JvmField
    var id: String? = null
    var isQueue: Boolean = false
    var title: String? = null // Chỉ tồn tại với type là group
    var avatar: String? = null // Chỉ tồn tại với type là group
    var theme: String? = null
    var type = RoomChatType.SINGLE

    @Transient // Field này sẽ không được lưu vào database
    var newRow: Boolean = false// Field đại diện cho object này có phải mới hay không.

    // Method để cho `R2dbcEntityTemplate` phân biệt và thực hiện lệnh `insert` hay `save` cho đúng
    @Override
    @Transient
    @JsonIgnore
    override fun isNew(): Boolean = this.newRow

    @Transient
    @JsonIgnore
    var pin: Boolean = false
    override fun getId(): String? = id;

    enum class RoomChatType {
        SINGLE, GROUP
    }
}