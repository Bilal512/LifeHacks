package com.simbaone.lifehacks.data.repo

import android.util.Log
import com.simbaone.lifehacks.domain.repo.LogRepo

class LogRepoImpl: LogRepo {

    override fun logError(msg: String) {
        try {
            val stackTrace = Thread.currentThread().stackTrace
            val callerClassName = stackTrace[3].className
            val callerSimpleClassName = callerClassName.substring(callerClassName.lastIndexOf('.') + 1)
            val callerMethodName = stackTrace[3].methodName
            val tag = "$callerSimpleClassName.$callerMethodName"
            Log.e(tag, msg)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}