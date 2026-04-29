package com.inseong.dallyrun.core.domain.auth

import javax.inject.Inject

class DeleteMemberUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {

    suspend operator fun invoke(password: String) {
        authRepository.deleteMember(password)
    }
}
