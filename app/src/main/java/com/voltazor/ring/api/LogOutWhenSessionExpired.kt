package com.voltazor.ring.api

import com.google.gson.Gson
import com.voltazor.ring.App
import retrofit2.HttpException
import rx.Observable
import rx.functions.Func1
import timber.log.Timber

/**
 * Created by voltazor on 03/10/17.
 */
class LogOutWhenSessionExpired : Func1<Observable<out Throwable>, Observable<*>> {

    override fun call(observable: Observable<out Throwable>): Observable<*> {
        return observable.flatMap(object : Func1<Throwable, Observable<*>> {

            var retriesCount = 1

            override fun call(throwable: Throwable?): Observable<*> {
                if (retriesCount-- > 0 && throwable is HttpException) {
                    val errorBody = Gson().fromJson(throwable.response().errorBody()?.string(), ErrorBody::class.java)
                    errorBody?.let { Timber.e("${it.error}: ${it.message}") }
                    val code = throwable.code()
                    return if (code == 403) App.apiManager.refreshToken() else Observable.error<Throwable>(throwable)
                }
                return Observable.error<Throwable>(throwable)
            }

        })
    }

}