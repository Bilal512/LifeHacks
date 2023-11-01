package com.simbaone.lifehacks.app

import android.app.Application
import com.simbaone.SimbaAds
import com.simbaone.lifehacks.BuildConfig
import com.simbaone.simba_ads.AdNetwork
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App: Application() {

    override fun onCreate() {
        super.onCreate()

        SimbaAds.builder()
            .setIsDebug(BuildConfig.DEBUG)
            .setTestDeviceId("eea6402a-8b32-4cf0-a83f-1cc7064b0921")
            .setAdmobInterstitialAdId("982919246150537_982920819483713")
            .setAdNetwork(AdNetwork.FACEBOOK)
            .build(this)
    }

}