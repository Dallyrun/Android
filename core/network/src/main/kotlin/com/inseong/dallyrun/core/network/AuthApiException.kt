package com.inseong.dallyrun.core.network

class AuthApiException(
    val statusCode: Int,
    message: String,
) : RuntimeException(message)
