package com.inseong.dallyrun.core.domain.auth

import com.inseong.dallyrun.core.model.AuthToken
import javax.inject.Inject

class SignupUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {

    suspend operator fun invoke(
        email: String,
        password: String,
        nickname: String,
        profileImageUri: String?,
    ): AuthToken = authRepository.signup(email, password, nickname, profileImageUri)
}
