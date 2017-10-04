package com.voltazor.ring.flow.auth

import android.net.Uri
import com.voltazor.ring.base.IBaseMvpPresenter

/**
 * Created by voltazor on 03/10/17.
 */
interface ILoginPresenter: IBaseMvpPresenter<ILoginView> {

    fun login()

    fun skipLogin()

    fun handleToken(uri: Uri)

}