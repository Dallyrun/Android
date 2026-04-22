package com.inseong.dallyrun.core.domain.auth

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PasswordValidatorTest {

    @Test
    fun `should reject password shorter than 8`() {
        assertFalse(isValidPassword("a".repeat(7)))
    }

    @Test
    fun `should accept password of length 8`() {
        assertTrue(isValidPassword("a".repeat(8)))
    }

    @Test
    fun `should accept password of length 100`() {
        assertTrue(isValidPassword("a".repeat(100)))
    }

    @Test
    fun `should reject password longer than 100`() {
        assertFalse(isValidPassword("a".repeat(101)))
    }

    @Test
    fun `should reject empty password`() {
        assertFalse(isValidPassword(""))
    }
}
