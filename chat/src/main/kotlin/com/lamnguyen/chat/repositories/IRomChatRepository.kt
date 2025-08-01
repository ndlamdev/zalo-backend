/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 5:13 PM-28/07/2025
 *  User: kimin
 **/

package com.lamnguyen.chat.repositories

import com.lamnguyen.chat.entities.RoomChat
import org.springframework.data.r2dbc.repository.R2dbcRepository

interface IRomChatRepository : R2dbcRepository<RoomChat, Long>