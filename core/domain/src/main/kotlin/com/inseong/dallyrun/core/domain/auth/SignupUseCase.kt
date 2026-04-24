package com.inseong.dallyrun.core.domain.auth

import com.inseong.dallyrun.core.model.AgeGroup
import com.inseong.dallyrun.core.model.AuthToken
import com.inseong.dallyrun.core.model.Gender
import javax.inject.Inject

class SignupUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {

    suspend operator fun invoke(
        email: String,
        password: String,
        nickname: String,
        profileImageUri: String?,
        ageGroup: AgeGroup,
        gender: Gender,
    ): AuthToken = authRepository.signup(
        email = email,
        password = password,
        nickname = nickname,
        profileImageUri = profileImageUri,
        ageGroup = ageGroup,
        gender = gender,
    )
}
