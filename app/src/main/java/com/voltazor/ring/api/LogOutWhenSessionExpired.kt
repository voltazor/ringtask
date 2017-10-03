package com.voltazor.ring.api

import com.google.gson.Gson
import retrofit2.HttpException
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Func1
import timber.log.Timber

/**
 * Created by voltazor on 03/10/17.
 */
class LogOutWhenSessionExpired : Func1<Observable<out Throwable>, Observable<*>> {

    override fun call(observable: Observable<out Throwable>): Observable<*> {
        return observable.observeOn(AndroidSchedulers.mainThread()).flatMap(Func1<Throwable, Observable<*>> { throwable ->
            if (throwable is HttpException) {
                val errorBody = Gson().fromJson(throwable.response().errorBody()?.string(), ErrorBody::class.java)
                errorBody?.let { Timber.e("${it.error}: ${it.message}") }
                val code = throwable.code()
                return@Func1 if (code == 403) {
                    Observable.empty<Any>().observeOn(AndroidSchedulers.mainThread()).doOnCompleted {
                        Timber.e("Session expired")
                    }
                } else Observable.error<Throwable>(throwable)
            }
            Observable.error<Throwable>(throwable)
        })
    }

}