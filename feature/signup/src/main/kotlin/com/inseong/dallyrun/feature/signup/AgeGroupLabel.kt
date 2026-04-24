package com.inseong.dallyrun.feature.signup

import androidx.annotation.StringRes
import com.inseong.dallyrun.core.model.AgeGroup
import com.inseong.dallyrun.core.model.Gender

@StringRes
internal fun AgeGroup.labelRes(): Int = when (this) {
    AgeGroup.TWENTIES -> R.string.signup_age_twenties
    AgeGroup.THIRTIES -> R.string.signup_age_thirties
    AgeGroup.FORTIES -> R.string.signup_age_forties
    AgeGroup.FIFTIES -> R.string.signup_age_fifties
    AgeGroup.SIXTIES_OR_OVER -> R.string.signup_age_sixties_or_over
}

@StringRes
internal fun Gender.labelRes(): Int = when (this) {
    Gender.MALE -> R.string.signup_gender_male
    Gender.FEMALE -> R.string.signup_gender_female
}
