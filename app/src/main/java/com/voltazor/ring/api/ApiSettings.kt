package com.voltazor.ring.api

import android.util.Base64

/**
 * Created by voltazor on 27/09/17.
 */
object ApiSettings {

    private const val OAUTH_BASE_URL = "https://www.reddit.com/api/v1/authorize.compact"
    private const val CLIENT_ID = "RetNxRJO2EP9Lw"
    const val REDIRECT_URI = "ringtest://callback"

    fun createAuthUrl(state: String) =
            "$OAUTH_BASE_URL?client_id=$CLIENT_ID&duration=permanent&response_type=code&state=$state&redirect_uri=$REDIRECT_URI&scope=read"

    val BASIC_AUTH = "Basic ${Base64.encodeToString("$CLIENT_ID:".toByteArray(), Base64.NO_WRAP)}"

    private const val SCHEME = "https://"
    private const val HOSTNAME = "www.reddit.com"
    private const val OAUTH_HOSTNAME = "oauth.reddit.com"
    private const val SSL_HOSTNAME = "ssl.reddit.com"

    private const val API = "/api"
    private const val V1 = "/v1"
    const val ACCESS_TOKEN = "$API$V1/access_token"

    const val BASE_URL = "$SCHEME$HOSTNAME"
    const val AUTH_BASE_URL = "$SCHEME$OAUTH_HOSTNAME"
    const val SSL_BASE_URL = "$SCHEME$SSL_HOSTNAME"

    private const val TYPE = "json"

    const val TOP = "/top.$TYPE"

    const val AFTER = "after"
    const val LIMIT = "limit"
    const val COUNT = "count"

}