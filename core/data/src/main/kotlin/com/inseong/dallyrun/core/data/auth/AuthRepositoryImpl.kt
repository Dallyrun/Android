package com.inseong.dallyrun.core.data.auth

import com.inseong.dallyrun.core.domain.auth.AuthRepository
import com.inseong.dallyrun.core.model.AgeGroup
import com.inseong.dallyrun.core.model.AuthToken
import com.inseong.dallyrun.core.model.Gender
import com.inseong.dallyrun.core.network.AuthApi
import com.inseong.dallyrun.core.network.AuthApiErrorParser
import com.inseong.dallyrun.core.network.SignupMultipartBuilder
import com.inseong.dallyrun.core.network.model.LoginRequest
import com.inseong.dallyrun.core.network.model.TokenRefreshRequest
import com.inseong.dallyrun.core.network.model.toDomain
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val tokenManager: TokenManagerImpl,
    private val signupMultipartBuilder: SignupMultipartBuilder,
    private val imageMultipartFactory: ImageMultipartFactory,
    private val errorParser: AuthApiErrorParser,
) : AuthRepository {

    override suspend fun loginWithEmail(email: String, password: String): AuthToken {
        val response = errorParser.wrap(::loginFallbackMessage) {
            authApi.login(LoginRequest(email = email, password = password))
        }
        val token = response.data.toDomain()
        tokenManager.saveTokens(token.accessToken, token.refreshToken)
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
        val uriString = requireNotNull(profileImageUri) { "프로필 이미지를 선택해주세요" }
        val dataPart = signupMultipartBuilder.buildDataPart(
            email = email,
            password = password,
            nickname = nickname,
            ageBracket = ageGroup.serverValue,
            gender = gender.name,
        )
        val imagePart = imageMultipartFactory.create(partName = "image", uriString = uriString)

        val response = errorParser.wrap(::signupFallbackMessage) {
            authApi.signup(data = dataPart, image = imagePart)
        }
        val token = response.data.toDomain()
        tokenManager.saveTokens(token.accessToken, token.refreshToken)
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

    private fun loginFallbackMessage(code: Int): String = when (code) {
        401 -> "이메일 또는 비밀번호가 올바르지 않아요"
        else -> "로그인에 실패했어요 (오류 $code)"
    }

    private fun signupFallbackMessage(code: Int): String = when (code) {
        400 -> "입력값을 다시 확인해주세요"
        409 -> "이미 사용 중인 이메일 또는 닉네임이에요"
        else -> "회원가입에 실패했어요 (오류 $code)"
    }
}
