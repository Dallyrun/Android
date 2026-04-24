package com.inseong.dallyrun.core.data.auth

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ImageMultipartFactory @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    fun create(partName: String, uriString: String): MultipartBody.Part {
        val uri = Uri.parse(uriString)
        val resolver = context.contentResolver
        val mimeType = resolver.getType(uri) ?: DEFAULT_MIME_TYPE
        val bytes = resolver.openInputStream(uri)?.use { it.readBytes() }
            ?: error("프로필 이미지를 불러올 수 없어요")
        val extension = when {
            mimeType.contains("png", ignoreCase = true) -> "png"
            mimeType.contains("webp", ignoreCase = true) -> "webp"
            else -> "jpg"
        }
        return MultipartBody.Part.createFormData(
            name = partName,
            filename = "profile.$extension",
            body = bytes.toRequestBody(mimeType.toMediaType()),
        )
    }

    private companion object {
        const val DEFAULT_MIME_TYPE = "image/jpeg"
    }
}
