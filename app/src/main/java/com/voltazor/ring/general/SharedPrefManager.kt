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
        private val REFRESH_TOKEN = "refresh_token"
        private val ANONYMOUS = "anonymous"

    }

    private var cachedValues = HashSet<CachedValue<*>>()
    private lateinit var cachedToken: CachedValue<String>
    private lateinit var cachedRefreshToken: CachedValue<String>
    private lateinit var cachedIsAnonymous: CachedValue<Boolean>

    var token: String?
        get() = cachedToken.value
        set(value) {
            cachedToken.value = value
        }

    var refreshToken: String?
        get() = cachedRefreshToken.value
        set(value) {
            cachedRefreshToken.value = value
        }

    var isAnonymous: Boolean
        get() = cachedIsAnonymous.value ?: false
        set(value) {
            cachedIsAnonymous.value = value
        }

    fun init(context: Context) {
        CachedValue.initialize(context.getSharedPreferences(NAME, Context.MODE_PRIVATE))
        CachedValue(TOKEN, String::class.java).also {
            cachedValues.add(it)
            cachedToken = it
        }
        CachedValue(REFRESH_TOKEN, String::class.java).also {
            cachedValues.add(it)
            cachedRefreshToken = it
        }
        CachedValue(ANONYMOUS, Boolean::class.java).also {
            cachedValues.add(it)
            cachedIsAnonymous = it
        }
    }

    fun clearAuth() {
        token = null
        refreshToken = null
    }

}