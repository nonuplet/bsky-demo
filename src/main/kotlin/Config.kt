package com.rirfee

import java.io.FileInputStream
import java.io.IOException
import java.util.*

object Config {
    private val properties = Properties()

    init {
        val file = "local.properties"
        try {
            FileInputStream(file).use { properties.load(it) }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    val BSKY_IDENTIFIER: String = properties.getProperty("BSKY_IDENTIFIER") ?: ""
    val BSKY_PASSWORD: String = properties.getProperty("BSKY_PASSWORD") ?: ""
}