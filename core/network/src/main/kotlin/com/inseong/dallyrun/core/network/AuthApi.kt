package com.inseong.dallyrun.core.network

import com.inseong.dallyrun.core.network.model.ApiResponse
import com.inseong.dallyrun.core.network.model.LoginRequest
import com.inseong.dallyrun.core.network.model.NetworkTokenResponse
import com.inseong.dallyrun.core.network.model.TokenRefreshRequest
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface AuthApi {

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): ApiResponse<NetworkTokenResponse>

    @Multipart
    @POST("api/auth/signup")
    suspend fun signup(
        @Part data: MultipartBody.Part,
        @Part image: MultipartBody.Part,
    ): ApiResponse<NetworkTokenResponse>

    @POST("api/auth/refresh")
    suspend fun refreshToken(@Body request: TokenRefreshRequest): ApiResponse<NetworkTokenResponse>

    @DELETE("api/auth/logout")
    suspend fun logout()
}
