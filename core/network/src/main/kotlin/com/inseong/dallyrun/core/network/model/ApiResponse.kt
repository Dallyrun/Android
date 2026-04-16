package com.inseong.dallyrun.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T>(
    val data: T,
)
