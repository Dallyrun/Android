package com.inseong.dallyrun.core.domain.auth

import com.inseong.dallyrun.core.model.AuthToken
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun loginWithEmail(email: String, password: String): AuthToken

    suspend fun signup(
        email: String,
        password: String,
        nickname: String,
        profileImageUri: String?,
    ): AuthToken

    suspend fun refreshToken(): AuthToken

    suspend fun logout()

    fun isLoggedIn(): Flow<Boolean>
}
