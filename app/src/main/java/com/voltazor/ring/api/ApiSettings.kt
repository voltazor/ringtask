package com.voltazor.ring.api

/**
 * Created by voltazor on 27/09/17.
 */
object ApiSettings {

    private const val OAUTH_BASE_URL = "https://www.reddit.com/api/v1/authorize.compact"
    private const val CLIENT_ID = "RetNxRJO2EP9Lw"
    const val REDIRECT_URI = "ringtest://callback"

    fun createAuthUrl(state: String) =
            "$OAUTH_BASE_URL?client_id=$CLIENT_ID&response_type=token&state=$state&redirect_uri=$REDIRECT_URI&scope=read"

    private const val SCHEME = "http://"
    private const val HOSTNAME = "www.reddit.com"

    const val BASE_URL = "$SCHEME$HOSTNAME"

    const val USER = "user"

    private const val TYPE = "json"

    const val TOP = "/top.$TYPE"

}