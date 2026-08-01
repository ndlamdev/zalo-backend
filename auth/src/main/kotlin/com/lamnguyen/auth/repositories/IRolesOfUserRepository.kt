/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 2:40 PM-14/06/2026
 *  User: ndlamdev
 **/

package com.lamnguyen.auth.repositories

import com.lamnguyen.auth.model.RolesOfUser
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface IRolesOfUserRepository:ReactiveCrudRepository<RolesOfUser, String> {
}