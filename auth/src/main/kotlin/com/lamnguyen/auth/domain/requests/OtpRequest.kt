package com.lamnguyen.auth.domain.requests

data class OtpRequest(val phoneNumber: String, val otp: String)
