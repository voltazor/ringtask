package com.voltazor.ring.general

import android.content.Context
import java.util.HashSet

/**
 * Created by voltazor on 28/09/17.
 */
class SharedPrefManager {

    companion object {

        private val NAME = "sharedPrefs"

        private val TOKEN = "token"

    }

    private var cachedValues = HashSet<CachedValue<*>>()
    private lateinit var cachedToken: CachedValue<String>

    var token: String?
        get() = cachedToken.value
        set(value) {
            cachedToken.value = value
        }

    fun init(context: Context) {
        CachedValue.initialize(context.getSharedPreferences(NAME, Context.MODE_PRIVATE))
        CachedValue(TOKEN, String::class.java).also {
            cachedValues.add(it)
            cachedToken = it
        }
    }

}