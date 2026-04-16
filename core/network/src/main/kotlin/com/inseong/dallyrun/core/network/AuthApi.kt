package com.inseong.dallyrun.core.network

import com.inseong.dallyrun.core.network.model.ApiResponse
import com.inseong.dallyrun.core.network.model.NetworkTokenResponse
import com.inseong.dallyrun.core.network.model.OAuthLoginRequest
import com.inseong.dallyrun.core.network.model.TokenRefreshRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST

interface AuthApi {

    @POST("api/auth/oauth/kakao")
    suspend fun loginWithKakao(@Body request: OAuthLoginRequest): ApiResponse<NetworkTokenResponse>

    @POST("api/auth/refresh")
    suspend fun refreshToken(@Body request: TokenRefreshRequest): ApiResponse<NetworkTokenResponse>

    @DELETE("api/auth/logout")
    suspend fun logout()
}
