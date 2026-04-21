package com.inseong.dallyrun.core.network.interceptor

import com.inseong.dallyrun.core.network.TokenProvider
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class AuthInterceptorTest {

    private val tokenProvider = mockk<TokenProvider>()
    private val interceptor = AuthInterceptor(tokenProvider)

    @Test
    fun `should add authorization header when token is available`() {
        coEvery { tokenProvider.getAccessToken() } returns "test-access-token"

        val chain = createChain("https://example.com/runs")
        interceptor.intercept(chain)

        val captured = chain.capturedRequest
        assertEquals("Bearer test-access-token", captured?.header("Authorization"))
    }

    @Test
    fun `should skip auth header for auth endpoints`() {
        val chain = createChain("https://example.com/api/auth/refresh")
        interceptor.intercept(chain)

        val captured = chain.capturedRequest
        assertNull(captured?.header("Authorization"))
    }

    @Test
    fun `should proceed without header when token is null`() {
        coEvery { tokenProvider.getAccessToken() } returns null

        val chain = createChain("https://example.com/runs")
        interceptor.intercept(chain)

        val captured = chain.capturedRequest
        assertNull(captured?.header("Authorization"))
    }

    private fun createChain(url: String): FakeChain = FakeChain(
        Request.Builder().url(url.toHttpUrl()).build(),
    )

    private class FakeChain(
        private val request: Request,
    ) : Interceptor.Chain {
        var capturedRequest: Request? = null

        override fun request(): Request = request

        override fun proceed(request: Request): Response {
            capturedRequest = request
            return Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("OK")
                .build()
        }

        override fun connection() = null
        override fun call() = mockk<okhttp3.Call>(relaxed = true)
        override fun connectTimeoutMillis() = 30000
        override fun readTimeoutMillis() = 30000
        override fun writeTimeoutMillis() = 30000
        override fun withConnectTimeout(timeout: Int, unit: java.util.concurrent.TimeUnit) = this
        override fun withReadTimeout(timeout: Int, unit: java.util.concurrent.TimeUnit) = this
        override fun withWriteTimeout(timeout: Int, unit: java.util.concurrent.TimeUnit) = this
    }
}
