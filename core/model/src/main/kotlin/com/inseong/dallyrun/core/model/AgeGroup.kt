package com.inseong.dallyrun.core.model

/**
 * 회원가입 시 사용자가 선택하는 나이대.
 *
 * @property serverValue 백엔드에 전송되는 숫자값 (20대 → 20, 60대 이상 → 60)
 */
enum class AgeGroup(val serverValue: Int) {
    TWENTIES(20),
    THIRTIES(30),
    FORTIES(40),
    FIFTIES(50),
    SIXTIES_OR_OVER(60),
}
