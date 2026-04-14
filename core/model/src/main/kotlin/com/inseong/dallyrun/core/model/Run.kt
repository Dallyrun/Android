package com.inseong.dallyrun.core.model

import kotlinx.serialization.Serializable

@Serializable
data class Run(
    val id: Long = 0L,
    val startTimeMillis: Long = 0L,
    val endTimeMillis: Long = 0L,
    val distanceMeters: Double = 0.0,
    val durationMillis: Long = 0L,
    val avgPaceMinPerKm: Double = 0.0,
    val calories: Int = 0,
    val route: List<Location> = emptyList(),
)
