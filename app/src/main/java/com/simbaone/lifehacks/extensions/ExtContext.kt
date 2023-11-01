package com.simbaone.lifehacks.extensions

import android.content.Context
import android.content.ContextWrapper
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.simbaone.lifehacks.R

fun Context.getDrawableByName(drawableName: String): Drawable? {
    val resources = this.resources
    val resourceId = resources.getIdentifier(drawableName, "drawable", this.packageName)
    return if (resourceId != 0) {
        ResourcesCompat.getDrawable(this.resources, resourceId, null)
    } else {
        null
    }
}

fun Context.getColor(@ColorRes id: Int): Int {
    return ContextCompat.getColor(this, id)
}