package com.voltazor.ring.flow.auth

import com.voltazor.ring.base.IBaseMvpView

/**
 * Created by voltazor on 03/10/17.
 */
interface ILoginView: IBaseMvpView {

    fun openUrl(url: String)

    fun navigateMain()

}