/**
 * Nguyen Dinh Lam
 * Email: kiminonawa1305@gmail.com
 * Phone number: +84 855354919
 * Create at: 7:28 PM-13/06/2026
 *  User: ndlamdev
 **/

package com.lamnguyen.auth.utils.helpers

import kotlin.random.Random

fun generateOtp(x: Int): String {
    if (x <= 0) return ""

    return List(x) { Random.nextInt(0, 10) }.joinToString("")
}