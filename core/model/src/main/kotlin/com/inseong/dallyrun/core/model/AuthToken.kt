package com.inseong.dallyrun.core.model

data class AuthToken(
    val accessToken: String,
    val refreshToken: String,
)
