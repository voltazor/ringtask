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
import com.voltazor.ring.api.dto.ListingData
import com.voltazor.ring.api.dto.ListingResponse
import com.voltazor.ring.api.service.AuthService
import com.voltazor.ring.api.service.NetworkService
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
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
        authService = createRetrofitBuilder().apply {
            baseUrl(SSL_BASE_URL)
            client(createClientBuilder().setupLogging().build())
        }.build().create(AuthService::class.java)

        networkService = createRetrofitBuilder().apply {
            baseUrl(BASE_URL)
            client(createClientBuilder().apply { addInterceptor(AuthInterceptor).setupLogging() }.build())
        }.build().create(NetworkService::class.java)
    }

    private fun createRetrofitBuilder() = Retrofit.Builder().apply {
        addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        addConverterFactory(createConverter())
    }

    private fun createConverter(): GsonConverterFactory {
        return GsonConverterFactory.create(GsonBuilder().apply {
            serializeNulls()
            registerTypeAdapter(ListingResponse::class.java, ListingResponseDeserializer())
        }.create())
    }

    private fun createClientBuilder(): OkHttpClient.Builder {
        return OkHttpClient.Builder().apply {
            readTimeout(60, TimeUnit.SECONDS)
            connectTimeout(60, TimeUnit.SECONDS)
            addInterceptor { chain ->
                val original = chain.request()
                chain.proceed(original.newBuilder().apply {
                    header("User-Agent", "android:com.voltazor.ring:v1.0.0 (by /u/voltazor)")
                    method(original.method(), original.body())
                }.build())
            }

        }
    }

    private fun OkHttpClient.Builder.setupLogging(): OkHttpClient.Builder {
        if (BuildConfig.DEBUG) {
            addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        }
        return this
    }

    //We need different hosts for authenticated/anonymous requests
    private object AuthInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            HttpUrl.parse(if (spManager.isAnonymous) BASE_URL else AUTH_BASE_URL)?.let {
                val original = chain.request()
                return chain.proceed(original.newBuilder().apply {
                    spManager.token?.let { header("Authorization", it) }
                    method(original.method(), original.body())
                    url(original.url().newBuilder().apply {
                        scheme(it.scheme())
                        host(it.url().toURI().host)
                        port(it.port())
                    }.build())
                }.build())
            }
            return chain.proceed(chain.request())
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

    fun requestTop(after: String?, count: Int): Observable<ListingData> = networkService.requestTop(after, count)
            .subscribeOn(Schedulers.io())
            .retryWhen(TokenRefresher())
            .map { t -> t.data }
            .observeOn(AndroidSchedulers.mainThread())

}