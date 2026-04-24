package com.inseong.dallyrun.core.data.auth

import com.inseong.dallyrun.core.domain.auth.AuthRepository
import com.inseong.dallyrun.core.model.AgeGroup
import com.inseong.dallyrun.core.model.AuthToken
import com.inseong.dallyrun.core.model.Gender
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
        // TODO: 백엔드 연동 시 AuthApi.loginWithEmail 호출로 교체.
        //  현재는 메인 화면 진입을 막지 않기 위한 dev stub — 실패 분기 없이 fake 토큰 발급.
        val token = PLACEHOLDER_TOKEN
        tokenManager.saveTokens(
            accessToken = token.accessToken,
            refreshToken = token.refreshToken,
        )
        return token
    }

    override suspend fun signup(
        email: String,
        password: String,
        nickname: String,
        profileImageUri: String?,
        ageGroup: AgeGroup,
        gender: Gender,
    ): AuthToken {
        // TODO: 백엔드 연동 시 AuthApi.signup 호출로 교체. 백엔드 전송 값:
        //  - ageGroup.serverValue (Int: 20/30/40/50/60)
        //  - gender.name (String: "MALE"/"FEMALE")
        //  - profileImageUri 가 null 이 아니면 multipart 업로드.
        //  현재는 회원가입 완료 후 메인 진입을 위한 dev stub.
        val token = PLACEHOLDER_TOKEN
        tokenManager.saveTokens(
            accessToken = token.accessToken,
            refreshToken = token.refreshToken,
        )
        return token
    }

    private companion object {
        // 백엔드 미구현 상태에서 클라 네비게이션/디자인 검증을 위한 placeholder.
        // 실제 API 연동 시 이 상수와 사용처 모두 제거.
        val PLACEHOLDER_TOKEN = AuthToken(
            accessToken = "dev-access-token",
            refreshToken = "dev-refresh-token",
        )
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
