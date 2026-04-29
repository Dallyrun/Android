package com.inseong.dallyrun.core.domain.auth

import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class DeleteMemberUseCaseTest {

    private val authRepository = mockk<AuthRepository>(relaxUnitFun = true)
    private val useCase = DeleteMemberUseCase(authRepository)

    @Test
    fun `should delegate password to repository deleteMember`() = runTest {
        useCase("p@ssw0rd")

        coVerify(exactly = 1) { authRepository.deleteMember("p@ssw0rd") }
    }
}
