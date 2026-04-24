package com.inseong.dallyrun.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiErrorBody(
    val message: String? = null,
)
