/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 12:50 PM-24/07/2025
 *  User: kimin
 **/

package com.lamnguyen.user.repositories

import com.lamnguyen.user.models.InviteAddFriend
import org.springframework.data.r2dbc.repository.R2dbcRepository

interface IInviteAddFriendRepository : R2dbcRepository<InviteAddFriend, String> {
}