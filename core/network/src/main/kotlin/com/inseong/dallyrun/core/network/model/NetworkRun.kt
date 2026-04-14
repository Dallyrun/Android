package com.inseong.dallyrun.core.network.model

import com.inseong.dallyrun.core.model.Location
import com.inseong.dallyrun.core.model.Run
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkRun(
    val id: Long = 0L,
    @SerialName("start_time_millis") val startTimeMillis: Long = 0L,
    @SerialName("end_time_millis") val endTimeMillis: Long = 0L,
    @SerialName("distance_meters") val distanceMeters: Double = 0.0,
    @SerialName("duration_millis") val durationMillis: Long = 0L,
    @SerialName("avg_pace_min_per_km") val avgPaceMinPerKm: Double = 0.0,
    val calories: Int = 0,
    val route: List<NetworkLocation> = emptyList(),
)

@Serializable
data class NetworkLocation(
    val latitude: Double,
    val longitude: Double,
    val altitude: Double = 0.0,
    @SerialName("timestamp_millis") val timestampMillis: Long = 0L,
)

fun NetworkRun.toDomain(): Run = Run(
    id = id,
    startTimeMillis = startTimeMillis,
    endTimeMillis = endTimeMillis,
    distanceMeters = distanceMeters,
    durationMillis = durationMillis,
    avgPaceMinPerKm = avgPaceMinPerKm,
    calories = calories,
    route = route.map { it.toDomain() },
)

fun NetworkLocation.toDomain(): Location = Location(
    latitude = latitude,
    longitude = longitude,
    altitude = altitude,
    timestampMillis = timestampMillis,
)
