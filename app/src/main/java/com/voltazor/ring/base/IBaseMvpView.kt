package com.voltazor.ring.base

import android.content.Context
import android.support.annotation.StringRes
import com.trello.navi.NaviComponent

/**
 * Created by voltazor on 03/10/17.
 */
interface IBaseMvpView : NaviComponent {

    fun getContext(): Context

    fun showError(error: String?)

    fun showError(@StringRes errorResId: Int)

    fun showMessage(message: String?)

    fun showMessage(@StringRes srtResId: Int)

}