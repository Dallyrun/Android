package com.inseong.dallyrun.core.network

import kotlinx.serialization.json.Json
import okio.Buffer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class SignupMultipartBuilderTest {

    private val builder = SignupMultipartBuilder(Json)

    @Test
    fun `should build data part with json content type and camelCase payload`() {
        val part = builder.buildDataPart(
            email = "user@example.com",
            password = "Abcd1234!",
            nickname = "달리기왕",
            ageBracket = 30,
            gender = "MALE",
        )

        val mediaType = part.body.contentType()
        assertNotNull(mediaType)
        assertEquals("application", mediaType!!.type)
        assertEquals("json", mediaType.subtype)

        val sink = Buffer()
        part.body.writeTo(sink)
        val json = sink.readUtf8()

        assertTrue("ageBracket field present (camelCase)", json.contains("\"ageBracket\":30"))
        assertTrue("gender uppercase enum", json.contains("\"gender\":\"MALE\""))
        assertTrue("email present", json.contains("\"email\":\"user@example.com\""))
        assertTrue("nickname present", json.contains("\"nickname\":\"달리기왕\""))
    }
}
