package com.simbaone.simba_ads.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.DisplayMetrics
import com.facebook.ads.AdSettings
import com.simbaone.simba_ads.SmSession

object SmUtils {

    fun isTablet(context: Context): Boolean {
        val metrics: DisplayMetrics = context.resources.displayMetrics
        val yInches = metrics.heightPixels / metrics.ydpi
        val xInches = metrics.widthPixels / metrics.xdpi
        val diagonalInches = Math.sqrt((xInches * xInches + yInches * yInches).toDouble())
        // 6.5inch device or bigger
        // smaller device
        return diagonalInches >= 6.5
    }

    fun setFacebookTestAds() {
        AdSettings.addTestDevice(SmSession.admobInterstitalAdId)
        AdSettings.setTestMode(SmSession.IS_DEBUG)
    }

    fun isConnected(context: Context): Boolean {
        val connectivity = context
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = connectivity.allNetworkInfo
        for (anInfo in info) {
            if (anInfo.state == NetworkInfo.State.CONNECTED) {
                return true
            }
        }
        return false
    }
}