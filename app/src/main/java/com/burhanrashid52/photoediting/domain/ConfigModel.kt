package com.burhanrashid52.photoediting.domain

import android.os.Parcelable
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize

data class ConfigModel(
    val toolsColor: String,
    val toolsNames: ToolsNamesModel,
    val stickers: List<StickerModel>
) {

    companion object {
        fun fromJson(json: String): ConfigModel {
            val gson = Gson()
            return gson.fromJson(json, ConfigModel::class.java)
        }
    }

}

data class ToolsNamesModel(
    val shape: String,
    val text: String,
    val eraser: String,
    val filter: String,
    val emoji: String,
    val sticker: String,
)

@Parcelize
data class StickerModel(
    val sourceType: String,
    val address: String
) : Parcelable