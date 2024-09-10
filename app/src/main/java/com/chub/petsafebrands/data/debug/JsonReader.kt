package com.chub.petsafebrands.data.debug

import android.content.res.AssetManager
import java.io.BufferedReader
import javax.inject.Inject

class JsonReader @Inject constructor(
    private val assetManager: AssetManager,
) {
    fun getJsonAsString(jsonFileName: String?): String {
        val jsonFilePath = String.format("%s", jsonFileName)

        val content = StringBuilder()
        assetManager.open(jsonFilePath).use {
            val reader = BufferedReader(it.reader())
            var line = reader.readLine()
            while (line != null) {
                content.append(line)
                line = reader.readLine()
            }
        }
        return content.toString()
    }

}