package com.inseong.dallyrun.core.domain.auth

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class NicknameValidatorTest {

    @Test
    fun `should accept Korean only`() {
        assertTrue(isValidNickname("러너"))
        assertTrue(isValidNickname("달리는인성"))
    }

    @Test
    fun `should accept English only`() {
        assertTrue(isValidNickname("runner"))
        assertTrue(isValidNickname("RUNNER"))
    }

    @Test
    fun `should accept mixed Korean English digits`() {
        assertTrue(isValidNickname("runner123"))
        assertTrue(isValidNickname("러너99"))
        assertTrue(isValidNickname("Run러너1"))
    }

    @Test
    fun `should reject length 1`() {
        assertFalse(isValidNickname("a"))
        assertFalse(isValidNickname("가"))
    }

    @Test
    fun `should accept length 2`() {
        assertTrue(isValidNickname("ab"))
        assertTrue(isValidNickname("가나"))
    }

    @Test
    fun `should accept length 12`() {
        assertTrue(isValidNickname("a".repeat(12)))
        assertTrue(isValidNickname("가".repeat(12)))
    }

    @Test
    fun `should reject length 13`() {
        assertFalse(isValidNickname("a".repeat(13)))
    }

    @Test
    fun `should reject empty`() {
        assertFalse(isValidNickname(""))
    }

    @Test
    fun `should reject whitespace`() {
        assertFalse(isValidNickname("a b"))
        assertFalse(isValidNickname("러너 인성"))
        assertFalse(isValidNickname(" runner"))
        assertFalse(isValidNickname("runner "))
    }

    @Test
    fun `should reject special characters`() {
        assertFalse(isValidNickname("run!"))
        assertFalse(isValidNickname("run@ner"))
        assertFalse(isValidNickname("러너_1"))
        assertFalse(isValidNickname("ru.nner"))
    }

    @Test
    fun `should reject Korean jamo (incomplete)`() {
        // ㄱ(U+3131) 같은 자모는 [가-힣] 범위 밖이므로 거부
        assertFalse(isValidNickname("ㄱㄴ"))
    }

    @Test
    fun `should reject emoji`() {
        assertFalse(isValidNickname("run🚀"))
    }

    @Test
    fun `should reject Chinese characters`() {
        assertFalse(isValidNickname("中文"))
    }
}
