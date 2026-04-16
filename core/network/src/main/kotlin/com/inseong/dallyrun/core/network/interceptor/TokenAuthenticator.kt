package com.inseong.dallyrun.core.network.interceptor

import com.inseong.dallyrun.core.network.AuthApi
import com.inseong.dallyrun.core.network.TokenProvider
import com.inseong.dallyrun.core.network.model.TokenRefreshRequest
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

internal class TokenAuthenticator @Inject constructor(
    private val tokenProvider: TokenProvider,
    private val authApi: AuthApi,
) : Authenticator {

    private val mutex = Mutex()

    override fun authenticate(route: Route?, response: Response): Request? {
        if (responseCount(response) >= MAX_RETRY) return null

        return runBlocking {
            mutex.withLock {
                val currentToken = tokenProvider.getAccessToken()
                val requestToken = response.request.header("Authorization")
                    ?.removePrefix("Bearer ")

                if (currentToken != null && currentToken != requestToken) {
                    retryWithToken(response, currentToken)
                } else {
                    refreshAndRetry(response)
                }
            }
        }
    }

    private fun retryWithToken(response: Response, token: String): Request =
        response.request.newBuilder()
            .header("Authorization", "Bearer $token")
            .build()

    private suspend fun refreshAndRetry(response: Response): Request? {
        val refreshToken = tokenProvider.getRefreshToken() ?: run {
            tokenProvider.clearTokens()
            return null
        }

        return try {
            val result = authApi.refreshToken(TokenRefreshRequest(refreshToken))
            tokenProvider.saveTokens(
                accessToken = result.data.accessToken,
                refreshToken = result.data.refreshToken,
            )
            response.request.newBuilder()
                .header("Authorization", "Bearer ${result.data.accessToken}")
                .build()
        } catch (_: Exception) {
            tokenProvider.clearTokens()
            null
        }
    }

    private fun responseCount(response: Response): Int {
        var count = 1
        var prior = response.priorResponse
        while (prior != null) {
            count++
            prior = prior.priorResponse
        }
        return count
    }

    private companion object {
        const val MAX_RETRY = 2
    }
}
