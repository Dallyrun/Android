package com.inseong.dallyrun.core.network.interceptor

import com.inseong.dallyrun.core.network.AuthApi
import com.inseong.dallyrun.core.network.TokenProvider
import com.inseong.dallyrun.core.network.model.ApiResponse
import com.inseong.dallyrun.core.network.model.NetworkTokenResponse
import com.inseong.dallyrun.core.network.model.TokenRefreshRequest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class TokenAuthenticatorTest {

    private val tokenProvider = mockk<TokenProvider>(relaxUnitFun = true)
    private val authApi = mockk<AuthApi>()
    private val authenticator = TokenAuthenticator(tokenProvider, authApi)

    @Test
    fun `should refresh token and retry on 401`() {
        coEvery { tokenProvider.getAccessToken() } returns "old-token"
        coEvery { tokenProvider.getRefreshToken() } returns "refresh-token"
        coEvery { authApi.refreshToken(TokenRefreshRequest("refresh-token")) } returns
            ApiResponse(NetworkTokenResponse("new-access", "new-refresh"))

        val response = create401Response(
            requestToken = "old-token",
        )

        val result = authenticator.authenticate(null, response)

        assertNotNull(result)
        assertEquals("Bearer new-access", result?.header("Authorization"))
        coVerify { tokenProvider.saveTokens(accessToken = "new-access", refreshToken = "new-refresh") }
    }

    @Test
    fun `should reuse already refreshed token`() {
        coEvery { tokenProvider.getAccessToken() } returns "already-refreshed-token"

        val response = create401Response(requestToken = "old-token")

        val result = authenticator.authenticate(null, response)

        assertNotNull(result)
        assertEquals("Bearer already-refreshed-token", result?.header("Authorization"))
        coVerify(exactly = 0) { authApi.refreshToken(any()) }
    }

    @Test
    fun `should return null when refresh token is missing`() {
        coEvery { tokenProvider.getAccessToken() } returns null
        coEvery { tokenProvider.getRefreshToken() } returns null

        val response = create401Response(requestToken = null)

        val result = authenticator.authenticate(null, response)

        assertNull(result)
        coVerify { tokenProvider.clearTokens() }
    }

    @Test
    fun `should clear tokens when refresh fails`() {
        coEvery { tokenProvider.getAccessToken() } returns "old-token"
        coEvery { tokenProvider.getRefreshToken() } returns "refresh-token"
        coEvery { authApi.refreshToken(any()) } throws RuntimeException("Network error")

        val response = create401Response(requestToken = "old-token")

        val result = authenticator.authenticate(null, response)

        assertNull(result)
        coVerify { tokenProvider.clearTokens() }
    }

    private fun create401Response(requestToken: String?): Response {
        val requestBuilder = Request.Builder()
            .url("https://example.com/runs")
        if (requestToken != null) {
            requestBuilder.header("Authorization", "Bearer $requestToken")
        }
        return Response.Builder()
            .request(requestBuilder.build())
            .protocol(Protocol.HTTP_1_1)
            .code(401)
            .message("Unauthorized")
            .build()
    }
}
