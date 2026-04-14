package com.inseong.dallyrun.core.model

import kotlinx.serialization.Serializable

@Serializable
data class Location(
    val latitude: Double,
    val longitude: Double,
    val altitude: Double = 0.0,
    val timestampMillis: Long = 0L,
)
