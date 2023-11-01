package com.simbaone.simba_ads.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

fun Context.getcolor(@ColorRes id: Int) = ContextCompat.getColor(this, id)

fun Context.scanForActivity(): Activity? {
    return when (this) {
        is AppCompatActivity -> this
        is ContextWrapper -> baseContext.scanForActivity()
        else -> null
    }
}