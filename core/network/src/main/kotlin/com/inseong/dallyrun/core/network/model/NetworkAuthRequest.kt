package com.inseong.dallyrun.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class TokenRefreshRequest(
    val refreshToken: String,
)

@Serializable
data class LoginRequest(
    val email: String,
    val password: String,
)

@Serializable
data class SignupData(
    val email: String,
    val password: String,
    val nickname: String,
    val ageBracket: Int,
    val gender: String,
)

@Serializable
data class DeleteMemberRequest(
    val password: String,
)
