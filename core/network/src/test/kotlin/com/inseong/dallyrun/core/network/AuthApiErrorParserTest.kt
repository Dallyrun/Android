package com.inseong.dallyrun.core.network

import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

class AuthApiErrorParserTest {

    private val parser = AuthApiErrorParser(Json { ignoreUnknownKeys = true })

    @Test
    fun `should pass through successful result`() = runTest {
        val result = parser.wrap(fallback = { "fallback" }) { 42 }
        assertEquals(42, result)
    }

    @Test
    fun `should parse server message field when present`() {
        val ex = assertThrows(AuthApiException::class.java) {
            kotlinx.coroutines.runBlocking {
                parser.wrap(fallback = { "default" }) {
                    throw httpException(409, """{"message":"중복된 이메일이에요"}""")
                }
            }
        }
        assertEquals(409, ex.statusCode)
        assertEquals("중복된 이메일이에요", ex.message)
    }

    @Test
    fun `should use fallback when error body is empty`() {
        val ex = assertThrows(AuthApiException::class.java) {
            kotlinx.coroutines.runBlocking {
                parser.wrap(fallback = { code -> "code=$code" }) {
                    throw httpException(401, "")
                }
            }
        }
        assertEquals("code=401", ex.message)
    }

    @Test
    fun `should use fallback when message field missing`() {
        val ex = assertThrows(AuthApiException::class.java) {
            kotlinx.coroutines.runBlocking {
                parser.wrap(fallback = { _ -> "default" }) {
                    throw httpException(400, """{"other":"x"}""")
                }
            }
        }
        assertEquals("default", ex.message)
    }

    private fun httpException(code: Int, body: String): HttpException =
        HttpException(Response.error<Any>(code, body.toResponseBody("application/json".toMediaType())))
}
