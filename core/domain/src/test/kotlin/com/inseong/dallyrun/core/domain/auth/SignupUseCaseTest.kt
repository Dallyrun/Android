package com.inseong.dallyrun.core.domain.auth

import com.inseong.dallyrun.core.model.AgeGroup
import com.inseong.dallyrun.core.model.AuthToken
import com.inseong.dallyrun.core.model.Gender
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class SignupUseCaseTest {

    private val authRepository = mockk<AuthRepository>()
    private val useCase = SignupUseCase(authRepository)

    @Test
    fun `should delegate to repository with all fields`() = runTest {
        val expected = AuthToken(accessToken = "access", refreshToken = "refresh")
        coEvery {
            authRepository.signup(
                email = "a@b.c",
                password = "Aa1!Aa1!",
                nickname = "runner",
                profileImageUri = "content://uri",
                ageGroup = AgeGroup.THIRTIES,
                gender = Gender.MALE,
            )
        } returns expected

        val result = useCase(
            email = "a@b.c",
            password = "Aa1!Aa1!",
            nickname = "runner",
            profileImageUri = "content://uri",
            ageGroup = AgeGroup.THIRTIES,
            gender = Gender.MALE,
        )

        assertEquals(expected, result)
        coVerify {
            authRepository.signup(
                email = "a@b.c",
                password = "Aa1!Aa1!",
                nickname = "runner",
                profileImageUri = "content://uri",
                ageGroup = AgeGroup.THIRTIES,
                gender = Gender.MALE,
            )
        }
    }

    @Test
    fun `should pass null profileImageUri through`() = runTest {
        val expected = AuthToken(accessToken = "a", refreshToken = "r")
        coEvery {
            authRepository.signup(
                email = "a@b.c",
                password = "Aa1!Aa1!",
                nickname = "runner",
                profileImageUri = null,
                ageGroup = AgeGroup.TWENTIES,
                gender = Gender.FEMALE,
            )
        } returns expected

        val result = useCase(
            email = "a@b.c",
            password = "Aa1!Aa1!",
            nickname = "runner",
            profileImageUri = null,
            ageGroup = AgeGroup.TWENTIES,
            gender = Gender.FEMALE,
        )

        assertEquals(expected, result)
    }
}
