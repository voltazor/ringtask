package com.voltazor.ring.api

import com.voltazor.ring.App
import retrofit2.HttpException
import rx.Observable
import rx.functions.Func1
import timber.log.Timber
import java.net.HttpURLConnection.HTTP_UNAUTHORIZED

/**
 * Created by voltazor on 03/10/17.
 */
class TokenRefresher : Func1<Observable<out Throwable>, Observable<*>> {

    override fun call(observable: Observable<out Throwable>): Observable<*> {
        return observable.flatMap(object : Func1<Throwable, Observable<*>> {

            var retriesCount = 1

            override fun call(throwable: Throwable?): Observable<*> {
                if (retriesCount-- > 0 && throwable is HttpException) {
                    if (throwable.code() == HTTP_UNAUTHORIZED) {
                        Timber.e("Session expired. Trying to refresh token...")
                        return App.apiManager.refreshToken()
                    }
                }
                return Observable.error<Throwable>(throwable)
            }

        })
    }

}