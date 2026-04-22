package com.inseong.dallyrun.core.domain.auth

import com.inseong.dallyrun.core.model.AuthToken
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {

    suspend operator fun invoke(email: String, password: String): AuthToken =
        authRepository.loginWithEmail(email, password)
}
