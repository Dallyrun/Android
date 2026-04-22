package com.inseong.dallyrun.core.domain.auth

const val PASSWORD_MIN_LENGTH = 8
const val PASSWORD_MAX_LENGTH = 100

fun isValidPassword(password: String): Boolean =
    password.length in PASSWORD_MIN_LENGTH..PASSWORD_MAX_LENGTH
