package com.voltazor.ring.api.service

import com.voltazor.ring.api.ApiSettings.ACCESS_TOKEN
import com.voltazor.ring.api.ApiSettings.BASIC_AUTH
import com.voltazor.ring.api.ApiSettings.REDIRECT_URI
import com.voltazor.ring.api.dto.AuthResponse
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query
import rx.Observable

/**
 * Created by voltazor on 04/10/17.
 */
interface AuthService {

    @POST(ACCESS_TOKEN)
    fun requestAccessToken(@Query("code") accessCode: String,
                           @Query("grant_type") authCode: String = "authorization_code",
                           @Query("redirect_uri") redirectUri: String = REDIRECT_URI,
                           @Header("Authorization") basicAuth: String = BASIC_AUTH): Observable<AuthResponse>

    @POST(ACCESS_TOKEN)
    fun refreshToken(@Query("refresh_token") refreshToken: String?,
                     @Query("grant_type") grantType: String = "refresh_token",
                     @Header("Authorization") basicAuth: String = BASIC_AUTH): Observable<AuthResponse>

}