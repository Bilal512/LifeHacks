package com.simbaone.lifehacks.utils

import android.content.Context
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject

class AssetReader @Inject constructor(private val context: Context) {

    fun getDataFromAssets(fileName: String): String {
        val jsonString = try {
            val inputStream = context.assets.open(fileName)
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            val stringBuilder = StringBuilder()
            bufferedReader.forEachLine { stringBuilder.append(it) }
            bufferedReader.close()
            inputStream.close()
            stringBuilder.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        }

        return jsonString
    }

}