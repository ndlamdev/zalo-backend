/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 11:43 AM-26/07/2026
 *  User: ndlamdev
 **/

package com.ndlamdev.chatwsrouter.utils.helper

object KeyGenerator {
    @Throws(Exception::class)
    fun generateSoftIdConversation(users: List<String>): String? {
        return Sha256Util.sha256(users.toSortedSet().joinToString(""))
    }
}