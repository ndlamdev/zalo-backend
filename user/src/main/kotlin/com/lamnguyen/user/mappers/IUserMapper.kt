/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 2:35 PM-08/08/2025
 *  User: kimin
 **/

package com.lamnguyen.user.mappers

import com.lamnguyen.user.domain.dto.UserDto
import com.lamnguyen.user.models.User
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface IUserMapper {
    fun toDto(user: User): UserDto
}