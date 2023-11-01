package com.simbaone.lifehacks.utils

import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import com.simbaone.lifehacks.app.App

object Utils {

    const val MAX_LIFE_HACK_LENGTH = 10060

    fun addReadMoreSpan(string: String): SpannableString {
        val maxLength = MAX_LIFE_HACK_LENGTH
        val readMore = " Read more"

        val spannableString = SpannableString(
            if (string.length > maxLength) string.substring(0, maxLength) + readMore else string
        )

        if (string.length > maxLength) {
            val readMoreEnd = maxLength + readMore.length
            val colorSpan = ForegroundColorSpan(Color.BLUE)
            spannableString.setSpan(colorSpan, maxLength, readMoreEnd, 0)
        }

        return spannableString
    }

    fun isConnected(context: Context): Boolean {
        val connectivity = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = connectivity.allNetworkInfo
        for (anInfo in info) {
            if (anInfo.state == NetworkInfo.State.CONNECTED) {
                return true
            }
        }
        return false
    }
}