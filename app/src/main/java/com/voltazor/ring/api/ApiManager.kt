package com.voltazor.ring.api

import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.GsonBuilder
import com.voltazor.ring.App.Companion.spManager
import com.voltazor.ring.BuildConfig
import com.voltazor.ring.api.deserializer.ListingResponseDeserializer
import com.voltazor.ring.api.deserializer.StringDeserializer
import com.voltazor.ring.api.dto.ListingResponse
import com.voltazor.ring.model.Listing
import io.realm.RealmObject
import okhttp3.OkHttpClient
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

    private lateinit var networkService: NetworkService

    fun init() {
        networkService = createRetrofit(createOkHttpClient()).create(NetworkService::class.java)
    }

    private fun createRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder().apply {
            addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            addConverterFactory(createGsonConverter())
            baseUrl(ApiSettings.BASE_URL)
            client(client)
        }.build()
    }

    private fun createGsonConverter(): GsonConverterFactory {
        return GsonConverterFactory.create(GsonBuilder().apply {
            serializeNulls()
            registerTypeAdapter(String::class.java, StringDeserializer())
            registerTypeAdapter(ListingResponse::class.java, ListingResponseDeserializer())
            setExclusionStrategies(object : ExclusionStrategy {
                override fun shouldSkipField(f: FieldAttributes) = f.declaringClass == RealmObject::class.java

                override fun shouldSkipClass(clazz: Class<*>) = false
            })
        }.create())
    }

    private fun createOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().apply {
            readTimeout(60, TimeUnit.SECONDS)
            connectTimeout(60, TimeUnit.SECONDS)
            if (BuildConfig.DEBUG) {
                addInterceptor { chain ->
                    val original = chain.request()
                    val request = original.newBuilder().apply {
                        header("User-Agent", "android:com.voltazor.ring:v1.0.0 (by /u/voltazor)")
                        spManager.token?.let { header("Authorization", it) }
                        method(original.method(), original.body())
                    }.build()
                    chain.proceed(request)
                }
                addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
            }
        }.build()
    }

    fun requestTop(): Observable<List<Listing>> {
        return networkService.requestTop()
                .subscribeOn(Schedulers.io())
                .retryWhen(LogOutWhenSessionExpired())
                .map { t -> t.data.children }
                .observeOn(AndroidSchedulers.mainThread())
    }

}