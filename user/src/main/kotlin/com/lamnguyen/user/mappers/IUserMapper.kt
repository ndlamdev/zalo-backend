/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 2:35 PM-08/08/2025
 *  User: kimin
 **/

package com.lamnguyen.user.mappers

import com.lamnguyen.user.domain.dto.UserInRelationShip
import com.lamnguyen.user.domain.request.RegisInfoRequest
import com.lamnguyen.user.models.User
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring")
interface IUserMapper {
    @Mapping(target = "displayName", source = "fullName")
    fun toUserInRelationShip(user: User): UserInRelationShip

    fun toUserInRelationShip(user: User, displayName: String): UserInRelationShip

    @Mapping(target = "avatar", source = "avatarUrl")
    fun toEntity(data: RegisInfoRequest): User
}