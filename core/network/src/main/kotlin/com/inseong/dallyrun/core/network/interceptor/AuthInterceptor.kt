package com.inseong.dallyrun.core.network.interceptor

import com.inseong.dallyrun.core.network.TokenProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

internal class AuthInterceptor @Inject constructor(
    private val tokenProvider: TokenProvider,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        if (request.url.encodedPath.contains("api/auth/")) {
            return chain.proceed(request)
        }

        val accessToken = runBlocking(Dispatchers.IO) { tokenProvider.getAccessToken() }
            ?: return chain.proceed(request)

        val authenticatedRequest = request.newBuilder()
            .header(AUTHORIZATION_HEADER, "$BEARER_PREFIX$accessToken")
            .build()

        return chain.proceed(authenticatedRequest)
    }
}
