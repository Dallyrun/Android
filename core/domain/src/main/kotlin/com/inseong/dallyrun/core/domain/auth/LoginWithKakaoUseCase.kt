package com.inseong.dallyrun.core.domain.auth

import com.inseong.dallyrun.core.model.AuthToken
import javax.inject.Inject

class LoginWithKakaoUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {

    suspend operator fun invoke(authCode: String): AuthToken =
        authRepository.loginWithKakao(authCode)
}
