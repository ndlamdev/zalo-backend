package com.lamnguyen.auth.events

data class OtpCreateAccountEvent(val phoneNumber: String, val otp: String)
