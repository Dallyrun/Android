package com.inseong.dallyrun.core.data.auth

import com.inseong.dallyrun.core.domain.auth.AuthRepository
import com.inseong.dallyrun.core.model.AuthToken
import com.inseong.dallyrun.core.network.AuthApi
import com.inseong.dallyrun.core.network.model.OAuthLoginRequest
import com.inseong.dallyrun.core.network.model.TokenRefreshRequest
import com.inseong.dallyrun.core.network.model.toDomain
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val tokenManager: TokenManagerImpl,
) : AuthRepository {

    override suspend fun loginWithKakao(authCode: String): AuthToken {
        val response = authApi.loginWithKakao(OAuthLoginRequest(authCode))
        val token = response.data.toDomain()
        tokenManager.saveTokens(
            accessToken = token.accessToken,
            refreshToken = token.refreshToken,
        )
        return token
    }

    override suspend fun refreshToken(): AuthToken {
        val refreshToken = requireNotNull(tokenManager.getRefreshToken()) {
            "Refresh token is not available"
        }
        val response = authApi.refreshToken(TokenRefreshRequest(refreshToken))
        val token = response.data.toDomain()
        tokenManager.saveTokens(
            accessToken = token.accessToken,
            refreshToken = token.refreshToken,
        )
        return token
    }

    override suspend fun logout() {
        try {
            authApi.logout()
        } finally {
            tokenManager.clearTokens()
        }
    }

    override fun isLoggedIn(): Flow<Boolean> = tokenManager.isLoggedIn()
}
