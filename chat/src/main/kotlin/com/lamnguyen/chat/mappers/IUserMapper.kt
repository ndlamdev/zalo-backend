/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 2:38 PM-30/07/2025
 *  User: kimin
 **/

package com.lamnguyen.chat.mappers

import com.lamnguyen.chat.protos.FriendShipCheck
import com.lamnguyen.chat.protos.FriendShipCheckRequest
import org.mapstruct.*

@Mapper(componentModel = "spring")
interface IUserMapper {
    @Mappings(
        Mapping(target = "friendShipsList", ignore = true)
    )
    fun toFriendShipCheckRequest(phoneNumberChecker: String, phoneNumberFriends: List<String>): FriendShipCheckRequest

    @AfterMapping
    fun afterToFriendShipCheckRequest(
        @MappingTarget builder: FriendShipCheckRequest.Builder,
        phoneNumberChecker: String,
        phoneNumberFriends: List<String>
    ) {
        val request = phoneNumberFriends
            .filter { it != phoneNumberChecker }
            .map {
                FriendShipCheck.newBuilder()
                    .apply {
                        this.phoneNumberChecker = phoneNumberChecker
                        phoneNumberFriend = it
                    }
                    .build()
            }

        builder.addAllFriendShips(request)
    }
}