package com.simbaone.lifehacks.utils

import android.graphics.Color
import java.lang.StrictMath.abs
import kotlin.random.Random

object ColorUtils {

    fun darkenColor(originalColor: Int, factor: Float = 0.4f): Int {
        val red = Color.red(originalColor)
        val green = Color.green(originalColor)
        val blue = Color.blue(originalColor)

        val darkenedRed = (red * factor).toInt().coerceIn(0, 255)
        val darkenedGreen = (green * factor).toInt().coerceIn(0, 255)
        val darkenedBlue = (blue * factor).toInt().coerceIn(0, 255)

        return Color.rgb(darkenedRed, darkenedGreen, darkenedBlue)
    }

    fun generateRandomColors(): Int {
        val randomColor = Color.rgb(Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))
        return darkenColor(randomColor, 0.8f)
    }

    sealed class AlphaValue(val value: Int) {
        init {
            require(value in 0..255) { "Alpha value must be between 0 and 255" }
        }
    }
    class ValidAlphaValue(value: Int) : AlphaValue(value)


    fun generateColorFromString(input: String): Int {
        val hash = input.hashCode()
        val red = abs(hash % 256)
        val green = abs((hash / 256) % 256)
        val blue = abs((hash / 256 / 256) % 256)
        return Color.rgb(red, green, blue)
    }

    fun getContrastColor(backgroundColor: Int): Int {
        val whiteContrast = androidx.core.graphics.ColorUtils.calculateContrast(Color.WHITE, backgroundColor)
        val blackContrast = androidx.core.graphics.ColorUtils.calculateContrast(Color.BLACK, backgroundColor)

        return if (whiteContrast > blackContrast) {
            Color.WHITE
        } else {
            Color.BLACK
        }
    }
}