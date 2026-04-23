package com.inseong.dallyrun.core.domain.auth

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PasswordValidatorTest {

    // ───── 백엔드 spec 샘플 ─────

    @Test
    fun `should accept Password123!`() {
        assertTrue(isValidPassword("Password123!"))
    }

    @Test
    fun `should reject password (only letters)`() {
        assertFalse(isValidPassword("password"))
    }

    @Test
    fun `should reject Password123 (no special)`() {
        assertFalse(isValidPassword("Password123"))
    }

    @Test
    fun `should reject Pass 1! (whitespace and length 7)`() {
        assertFalse(isValidPassword("Pass 1!"))
    }

    @Test
    fun `should reject Password1!가 (Korean)`() {
        assertFalse(isValidPassword("Password1!가"))
    }

    @Test
    fun `should reject 32 chars`() {
        assertFalse(isValidPassword("Aa1!" + "a".repeat(28)))
    }

    // ───── 길이 경계 ─────

    @Test
    fun `should reject length 7 with all classes`() {
        assertFalse(isValidPassword("Aa1!Aa1"))
    }

    @Test
    fun `should accept length 8 with all classes`() {
        assertTrue(isValidPassword("Aa1!Aa1!"))
    }

    @Test
    fun `should accept length 30 with all classes`() {
        // 4 + 26 = 30
        assertTrue(isValidPassword("Aa1!" + "a".repeat(26)))
    }

    @Test
    fun `should reject length 31`() {
        assertFalse(isValidPassword("Aa1!" + "a".repeat(27)))
    }

    @Test
    fun `should reject empty password`() {
        assertFalse(isValidPassword(""))
    }

    // ───── 클래스별 누락 ─────

    @Test
    fun `should reject when missing letter`() {
        assertFalse(isValidPassword("1234!@#$"))
    }

    @Test
    fun `should reject when missing digit`() {
        assertFalse(isValidPassword("Aabc!@#$"))
    }

    @Test
    fun `should reject when missing special`() {
        assertFalse(isValidPassword("Aabc1234"))
    }

    @Test
    fun `should reject only digits`() {
        assertFalse(isValidPassword("12345678"))
    }

    // ───── ASCII 특수기호 범위 검증 ─────

    @Test
    fun `should accept period as special`() {
        // '.' 은 ASCII 46 -> 특수기호 범위에 포함
        assertTrue(isValidPassword("Aa1.aaaa"))
    }

    @Test
    fun `should accept backtick as special`() {
        // '`' 은 ASCII 96 -> 특수기호 범위에 포함
        assertTrue(isValidPassword("Aa1`aaaa"))
    }

    @Test
    fun `should accept various ASCII specials`() {
        assertTrue(isValidPassword("Aa1{}@_~1"))
    }

    // ───── 비-허용 문자 거부 ─────

    @Test
    fun `should reject emoji`() {
        assertFalse(isValidPassword("Aa1!🚀aa"))
    }

    @Test
    fun `should reject fullwidth exclamation`() {
        // '！' 은 U+FF01 (전각) -> 비-ASCII
        assertFalse(isValidPassword("Aa1！aaaa"))
    }

    @Test
    fun `should reject control character`() {
        // BEL 은 U+0007 -> ASCII 33-126 밖
        assertFalse(isValidPassword("Aa1!\u0007aa"))
    }

    @Test
    fun `should reject tab`() {
        assertFalse(isValidPassword("Aa1!\taaa"))
    }
}
