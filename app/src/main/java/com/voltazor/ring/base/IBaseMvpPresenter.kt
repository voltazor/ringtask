package com.voltazor.ring.base

import android.os.Bundle

/**
 * Created by voltazor on 03/10/17.
 */
interface IBaseMvpPresenter<in V : IBaseMvpView> {

    fun attachView(view: V)

    fun restoreState(bundle: Bundle?)

    fun saveInstanceState(bundle: Bundle?)

    fun detachView()

}