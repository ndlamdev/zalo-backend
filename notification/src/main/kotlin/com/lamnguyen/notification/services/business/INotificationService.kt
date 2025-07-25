/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 5:03 PM-24/07/2025
 *  User: kimin
 **/

package com.lamnguyen.notification.services.business

import com.lamnguyen.notification.messages.InviteAddFriendMessage
import reactor.core.publisher.Mono

interface INotificationService {
    fun notifyInviteAddFriend(message: InviteAddFriendMessage): Mono<Void>
}