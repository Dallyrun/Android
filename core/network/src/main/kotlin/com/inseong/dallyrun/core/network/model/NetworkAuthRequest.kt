package com.inseong.dallyrun.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OAuthLoginRequest(
    @SerialName("auth_code") val authCode: String,
)

@Serializable
data class TokenRefreshRequest(
    @SerialName("refresh_token") val refreshToken: String,
)
