package com.inseong.dallyrun.core.domain.auth

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLoginStateUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {

    operator fun invoke(): Flow<Boolean> = authRepository.isLoggedIn()
}
