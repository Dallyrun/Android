package com.inseong.dallyrun.core.domain.auth

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class EmailValidatorTest {

    @Test
    fun `should accept standard email`() {
        assertTrue(isValidEmail("runner@dallyrun.com"))
    }

    @Test
    fun `should accept email with subdomain`() {
        assertTrue(isValidEmail("user@mail.example.co.kr"))
    }

    @Test
    fun `should reject empty string`() {
        assertFalse(isValidEmail(""))
    }

    @Test
    fun `should reject missing at sign`() {
        assertFalse(isValidEmail("runnerdallyrun.com"))
    }

    @Test
    fun `should reject missing dot in domain`() {
        assertFalse(isValidEmail("runner@dallyrun"))
    }

    @Test
    fun `should reject whitespace inside`() {
        assertFalse(isValidEmail("run ner@dallyrun.com"))
    }
}
