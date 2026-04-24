package com.inseong.dallyrun.core.domain.auth

const val NICKNAME_MIN_LENGTH = 2
const val NICKNAME_MAX_LENGTH = 12

/**
 * 닉네임 규칙: 한글(가-힣) / 영문(A-Za-z) / 숫자(0-9)만 허용, 2~12자.
 * 공백·특수문자·이모지·자모(ㄱㅏ 등)는 모두 거부.
 */
private val NICKNAME_REGEX = Regex(
    "^[가-힣A-Za-z0-9]{$NICKNAME_MIN_LENGTH,$NICKNAME_MAX_LENGTH}\$",
)

fun isValidNickname(nickname: String): Boolean = NICKNAME_REGEX.matches(nickname)
