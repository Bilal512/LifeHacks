package com.simbaone.lifehacks.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class LifeHackDTO(
    @SerializedName("data") val data: String?
): Parcelable
