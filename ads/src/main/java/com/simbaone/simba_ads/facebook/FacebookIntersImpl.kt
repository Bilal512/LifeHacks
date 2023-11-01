package com.simbaone.simba_ads.facebook

import android.app.Activity
import android.content.Context
import android.util.Log
import com.facebook.ads.Ad
import com.facebook.ads.InterstitialAd
import com.facebook.ads.InterstitialAdListener
import com.simbaone.simba_ads.SmSession
import com.simbaone.simba_ads.abstract.IntersAd
import com.simbaone.simba_ads.utils.SmUtils

class FacebookIntersImpl : IntersAd() {
    override val TAG = "FacebookInters"

    private var interstitialAd: InterstitialAd? = null

    override fun show(activity: Activity) {
        if (interstitialAd != null) {
            interstitialAd?.show()
        } else {
            adListener.onAdFailedToShow(AdError("facebook Inters Ad not loaded", 0))
        }
    }

    override fun load(context: Context) {
        SmUtils.setFacebookTestAds()
        interstitialAd = InterstitialAd(context, SmSession.admobInterstitalAdId)
        interstitialAd?.loadAd(
            interstitialAd!!.buildLoadAdConfig()
                .withAdListener(interstitialAdListener)
                .build()
        )
    }

    private val interstitialAdListener: InterstitialAdListener = object : InterstitialAdListener {
        override fun onInterstitialDisplayed(ad: Ad) {
            // Interstitial ad displayed callback
            Log.e(FacebookInters.TAG, "Interstitial ad displayed.")
            adListener.onAdShowed()
        }

        override fun onInterstitialDismissed(ad: Ad) {
            // Interstitial dismissed callback
            Log.e(FacebookInters.TAG, "Interstitial ad dismissed.")
            adListener.onAdDismissed()
        }


        override fun onError(ad: Ad?, adError: com.facebook.ads.AdError) {
            // Ad error callback
            Log.e(FacebookInters.TAG, "Interstitial ad failed to load: " + adError.errorMessage)
            adListener.onAdFailedToShow(IntersAd.AdError(adError.errorMessage, adError.errorCode))
        }

        override fun onAdLoaded(ad: Ad) {
            // Interstitial ad is loaded and ready to be displayed
            Log.d(FacebookInters.TAG, "Interstitial ad is loaded and ready to be displayed!")
        }

        override fun onAdClicked(ad: Ad) {
            // Ad clicked callback
            Log.d(FacebookInters.TAG, "Interstitial ad clicked!")
            adListener.onAdClicked()
        }

        override fun onLoggingImpression(ad: Ad) {
            // Ad impression logged callback
            Log.d(FacebookInters.TAG, "Interstitial ad impression logged!")
            adListener.onAdImpression()
        }
    }

}