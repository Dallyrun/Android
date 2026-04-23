package com.inseong.dallyrun.core.domain.auth

const val PASSWORD_MIN_LENGTH = 8
const val PASSWORD_MAX_LENGTH = 30

/**
 * 백엔드와 동일한 비밀번호 검증 정규식.
 *
 * - 영문자 [A-Za-z], 숫자 [0-9], ASCII 특수기호 각 1자 이상 포함
 * - 허용 문자는 위 세 집합(ASCII)만 — 공백/한글/이모지/전각/제어 문자 모두 거부
 * - 길이 8~30자
 *
 * ASCII 특수기호 범위 (영/숫 제외 ASCII 33~126):
 *   `!-/` `:-@` `[-`` `{-~`
 */
private val PASSWORD_REGEX = Regex(
    "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[!-/:-@\\[-`{-~])[A-Za-z0-9!-/:-@\\[-`{-~]{$PASSWORD_MIN_LENGTH,$PASSWORD_MAX_LENGTH}\$",
)

fun isValidPassword(password: String): Boolean = PASSWORD_REGEX.matches(password)
