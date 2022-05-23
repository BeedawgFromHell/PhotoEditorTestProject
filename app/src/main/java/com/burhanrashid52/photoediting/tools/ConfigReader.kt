package com.burhanrashid52.photoediting.tools

import android.content.Context
import com.burhanrashid52.photoediting.domain.ConfigModel
import java.io.File
import java.io.IOException

class ConfigReader {
    fun read(name: String, context: Context): ConfigModel? {
        return try {
            val json = context.assets.open(name).bufferedReader().readText()
            ConfigModel.fromJson(json)
        } catch (ex: IOException) {
            null
        }
    }
}