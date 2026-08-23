package com.lamnguyen.chat.domain.dto

import com.lamnguyen.chat.utils.enums.RelationShipStatus

class UserInRelationShip : UserDto() {
    var displayName: String? = null
    var relationShipStatus: RelationShipStatus = RelationShipStatus.SELF
}