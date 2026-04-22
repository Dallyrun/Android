package com.inseong.dallyrun.core.data.auth

import com.inseong.dallyrun.core.domain.auth.AuthRepository
import com.inseong.dallyrun.core.model.AuthToken
import com.inseong.dallyrun.core.network.AuthApi
import com.inseong.dallyrun.core.network.model.TokenRefreshRequest
import com.inseong.dallyrun.core.network.model.toDomain
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val tokenManager: TokenManagerImpl,
) : AuthRepository {

    override suspend fun loginWithEmail(email: String, password: String): AuthToken {
        // TODO: 후속 PR에서 AuthApi.loginWithEmail 엔드포인트 호출 + 토큰 저장 구현
        throw NotImplementedError("이메일/비밀번호 로그인 백엔드 연동은 후속 PR에서 진행")
    }

    override suspend fun signup(
        email: String,
        password: String,
        nickname: String,
        profileImageUri: String?,
    ): AuthToken {
        // TODO: 후속 PR에서 AuthApi.signup 엔드포인트 호출 + 프로필 이미지 업로드 + 토큰 저장 구현
        throw NotImplementedError("회원가입 백엔드 연동은 후속 PR에서 진행")
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
