package com.inseong.dallyrun.core.network

import com.inseong.dallyrun.core.network.model.NetworkRun
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface DallyrunApi {

    @GET("runs")
    suspend fun getRuns(): List<NetworkRun>

    @GET("runs/{id}")
    suspend fun getRun(@Path("id") id: Long): NetworkRun

    @POST("runs")
    suspend fun createRun(@Body run: NetworkRun): NetworkRun

    @DELETE("runs/{id}")
    suspend fun deleteRun(@Path("id") id: Long)
}
