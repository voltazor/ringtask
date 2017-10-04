package com.voltazor.ring.api.dto

import com.google.gson.annotations.SerializedName

/**
 * Created by voltazor on 04/10/17.
 */
data class AuthResponse(@SerializedName("access_token") val accessToken: String,
                        @SerializedName("token_type") val type: String,
                        @SerializedName("expires_in") val expires: Long,
                        @SerializedName("refresh_token") val refreshToken: String)