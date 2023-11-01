package com.simbaone.lifehacks.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CategoryDTO(
    @SerializedName("id") val id: String? = "",
    @SerializedName("name") val name: String? = "",
    @SerializedName("fileName") val fileName: String? = "",
    @SerializedName("iconName") val iconName: String? = "",
    @SerializedName("count") val count: Int? = 100,
    @SerializedName("credit") val credit: String? = null,
    @SerializedName("color") val bgColor: Int? = null,
): Parcelable
