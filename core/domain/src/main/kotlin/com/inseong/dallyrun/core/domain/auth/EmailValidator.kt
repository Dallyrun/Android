package com.inseong.dallyrun.core.domain.auth

private val EMAIL_REGEX = Regex("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")

fun isValidEmail(email: String): Boolean = EMAIL_REGEX.matches(email)
