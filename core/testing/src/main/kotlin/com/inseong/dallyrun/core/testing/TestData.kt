package com.inseong.dallyrun.core.testing

import com.inseong.dallyrun.core.model.Location
import com.inseong.dallyrun.core.model.Run

object TestData {

    val sampleLocation = Location(
        latitude = 37.5665,
        longitude = 126.9780,
        altitude = 38.0,
        timestampMillis = 1700000000000L,
    )

    val sampleRun = Run(
        id = 1L,
        startTimeMillis = 1700000000000L,
        endTimeMillis = 1700003600000L,
        distanceMeters = 5000.0,
        durationMillis = 3600000L,
        avgPaceMinPerKm = 12.0,
        calories = 350,
        route = listOf(sampleLocation),
    )

    val sampleRuns = listOf(
        sampleRun,
        sampleRun.copy(id = 2L, distanceMeters = 10000.0, durationMillis = 7200000L),
        sampleRun.copy(id = 3L, distanceMeters = 3000.0, durationMillis = 1800000L),
    )
}
