package com.voltazor.ring.flow.auth

import android.net.Uri
import android.os.Bundle
import com.voltazor.ring.App
import com.voltazor.ring.api.ApiSettings
import com.voltazor.ring.base.BaseMvpPresenterImpl
import timber.log.Timber

/**
 * Created by voltazor on 03/10/17.
 */
class LoginPresenter: BaseMvpPresenterImpl<ILoginView>(), ILoginPresenter {

    companion object {

        const val STATE_KEY = "state"

    }

    private var state: String? = null

    override fun login() {
        val state = generateState().apply { state = this }
        view?.openUrl(ApiSettings.createAuthUrl(state))
    }

    override fun handleToken(uri: Uri) {
        Timber.d("uri: $uri")
        if (uri.toString().startsWith(ApiSettings.REDIRECT_URI)) {
            state?.let {
                if (it == uri.getQueryParameter("state")) {
                    val accessCode = uri.getQueryParameter("code")
                    if (accessCode != null) {
                        requestToken(accessCode)
                    } else {
                        uri.getQueryParameter("error")?.let {
                            Timber.e(it)
                            view?.showError("Authorization failed")
                        }
                    }
                }
            }
        }
    }

    private fun requestToken(accessCode: String) {
        addSubscription(App.apiManager.requestToken(accessCode).subscribe({
            App.spManager.isAnonymous = false
            view?.navigateMain()
        }, {
            Timber.e(it, "Authorization failed")
            view?.showError("Authorization failed")
        }))
    }

    override fun skipLogin() {
        App.spManager.isAnonymous = true
        view?.navigateMain()
    }

    private fun generateState() = "state${System.currentTimeMillis()}"

    override fun restoreState(bundle: Bundle?) {
        bundle?.let { state = it.getString(STATE_KEY) }
    }

    override fun saveInstanceState(bundle: Bundle?) {
        bundle?.putString(STATE_KEY, state)
    }

}