package com.inseong.dallyrun.core.network.model

import com.inseong.dallyrun.core.model.AuthToken
import kotlinx.serialization.Serializable

@Serializable
data class NetworkTokenResponse(
    val accessToken: String,
    val refreshToken: String,
)

fun NetworkTokenResponse.toDomain(): AuthToken = AuthToken(
    accessToken = accessToken,
    refreshToken = refreshToken,
)
