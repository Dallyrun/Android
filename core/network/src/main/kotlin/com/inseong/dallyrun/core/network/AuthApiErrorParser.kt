package com.inseong.dallyrun.core.network

import com.inseong.dallyrun.core.network.model.ApiErrorBody
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthApiErrorParser @Inject constructor(
    private val json: Json,
) {

    suspend fun <T> wrap(fallback: (Int) -> String, block: suspend () -> T): T = try {
        block()
    } catch (e: HttpException) {
        throw AuthApiException(
            statusCode = e.code(),
            message = parseMessage(e) ?: fallback(e.code()),
        )
    }

    private fun parseMessage(e: HttpException): String? {
        val raw = runCatching { e.response()?.errorBody()?.string() }.getOrNull() ?: return null
        if (raw.isBlank()) return null
        return runCatching { json.decodeFromString<ApiErrorBody>(raw).message }
            .getOrNull()
            ?.takeIf { it.isNotBlank() }
    }
}
