package com.simbaone.lifehacks.domain.model

import android.text.SpannableString

data class LifeHack(
    val data: String,
    val spanText: SpannableString = SpannableString(""),
    val canReadMore: Boolean = false,
) {

}
