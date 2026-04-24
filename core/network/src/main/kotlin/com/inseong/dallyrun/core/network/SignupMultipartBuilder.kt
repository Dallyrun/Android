package com.inseong.dallyrun.core.network

import com.inseong.dallyrun.core.network.model.SignupData
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SignupMultipartBuilder @Inject constructor(
    private val json: Json,
) {

    fun buildDataPart(
        email: String,
        password: String,
        nickname: String,
        ageBracket: Int,
        gender: String,
    ): MultipartBody.Part {
        val payload = SignupData(
            email = email,
            password = password,
            nickname = nickname,
            ageBracket = ageBracket,
            gender = gender,
        )
        val body = json.encodeToString(payload).toRequestBody(JSON_MEDIA_TYPE)
        return MultipartBody.Part.createFormData(name = PART_NAME, filename = null, body = body)
    }

    private companion object {
        const val PART_NAME = "data"
        val JSON_MEDIA_TYPE = "application/json".toMediaType()
    }
}
