package com.simbaone.lifehacks.firebase

import android.content.Context
import android.os.Environment
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent

object EventLogger {

    val UI = "LifeHacks_UI_Android"
    val environment = "LifeHacks_Environment"

    fun logEventUI() {

    }

    fun logFirebaseUIEvent(context: Context, eventType:  EventTypeUI, value: String) {
        val firebaseAnalytics = FirebaseAnalytics.getInstance(context)

        firebaseAnalytics.logEvent(UI) {
            param(eventType.name, value)
        }
    }

}

enum class EventTypeUI(name: String) {
    CATEOGRY("category"),
}