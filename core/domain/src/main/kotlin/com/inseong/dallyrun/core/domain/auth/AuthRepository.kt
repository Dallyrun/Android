package com.inseong.dallyrun.core.domain.auth

import com.inseong.dallyrun.core.model.AuthToken
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun refreshToken(): AuthToken

    suspend fun logout()

    fun isLoggedIn(): Flow<Boolean>
}
