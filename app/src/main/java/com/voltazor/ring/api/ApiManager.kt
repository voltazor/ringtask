package com.voltazor.ring.api

import com.google.gson.GsonBuilder
import com.voltazor.ring.App
import com.voltazor.ring.App.Companion.spManager
import com.voltazor.ring.BuildConfig
import com.voltazor.ring.api.ApiSettings.AUTH_BASE_URL
import com.voltazor.ring.api.ApiSettings.BASE_URL
import com.voltazor.ring.api.ApiSettings.SSL_BASE_URL
import com.voltazor.ring.api.deserializer.ListingResponseDeserializer
import com.voltazor.ring.api.dto.AuthResponse
import com.voltazor.ring.api.dto.ListingResponse
import com.voltazor.ring.api.service.AuthService
import com.voltazor.ring.api.service.NetworkService
import com.voltazor.ring.model.Listing
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * Created by voltazor on 27/09/17.
 */
class ApiManager {

    private lateinit var authService: AuthService
    private lateinit var networkService: NetworkService

    fun init() {
        authService = createRetrofitBuilder(false).baseUrl(SSL_BASE_URL).build().create(AuthService::class.java)
        networkService = createRetrofitBuilder(true).baseUrl(BASE_URL).build().create(NetworkService::class.java)
    }

    private fun createRetrofitBuilder(anonymousMode: Boolean) = Retrofit.Builder().apply {
        addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        addConverterFactory(createGsonConverter())
        client(createOkHttpClient(anonymousMode))
    }

    private fun createGsonConverter(): GsonConverterFactory {
        return GsonConverterFactory.create(GsonBuilder().apply {
            serializeNulls()
            registerTypeAdapter(ListingResponse::class.java, ListingResponseDeserializer())
        }.create())
    }

    private fun createOkHttpClient(anonymousMode: Boolean): OkHttpClient {
        return OkHttpClient.Builder().apply {
            readTimeout(60, TimeUnit.SECONDS)
            connectTimeout(60, TimeUnit.SECONDS)
            addInterceptor { chain ->
                val original = chain.request()
                val request = original.newBuilder().apply {
                    if (anonymousMode) {
                        configureRequest(original, this)
                    }
                    header("User-Agent", "android:com.voltazor.ring:v1.0.0 (by /u/voltazor)")
                    spManager.token?.let { header("Authorization", it) }
                }.build()
                chain.proceed(request)
            }
            if (BuildConfig.DEBUG) {
                addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
            }
        }.build()
    }

    //We need different hosts for authenticated/anonymous requests
    private fun configureRequest(original: Request, builder: Request.Builder) {
        HttpUrl.parse(if (spManager.isAnonymous) BASE_URL else AUTH_BASE_URL)?.let {
            builder.url(original.url().newBuilder().apply {
                scheme(it.scheme())
                host(it.url().toURI().host)
                port(it.port())
            }.build())
            builder.method(original.method(), original.body())
        }
    }

    fun requestToken(accessCode: String): Observable<AuthResponse> = persistToken(authService.requestAccessToken(accessCode))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun refreshToken(): Observable<AuthResponse> = persistToken(authService.refreshToken(spManager.refreshToken))

    private fun persistToken(observable: Observable<AuthResponse>) = observable.doOnNext {
        App.spManager.token = "${it.type} ${it.accessToken}"
        App.spManager.refreshToken = it.refreshToken
    }

    fun requestTop(): Observable<List<Listing>> = networkService.requestTop()
            .subscribeOn(Schedulers.io())
            .retryWhen(LogOutWhenSessionExpired())
            .map { t -> t.data.children }
            .observeOn(AndroidSchedulers.mainThread())

}