package com.inseong.dallyrun.core.domain.auth

import com.inseong.dallyrun.core.model.AgeGroup
import com.inseong.dallyrun.core.model.AuthToken
import com.inseong.dallyrun.core.model.Gender
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun loginWithEmail(email: String, password: String): AuthToken

    suspend fun signup(
        email: String,
        password: String,
        nickname: String,
        profileImageUri: String?,
        ageGroup: AgeGroup,
        gender: Gender,
    ): AuthToken

    suspend fun refreshToken(): AuthToken

    suspend fun logout()

    suspend fun deleteMember(password: String)

    fun isLoggedIn(): Flow<Boolean>
}
