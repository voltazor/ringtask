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
                val map = parseUri(uri)
                if (it == map["state"]) {
                    App.spManager.token = map["access_token"]
                    if (App.spManager.token != null) {

                        view?.loginSucceed()
                    } else {
                        map["error"]?.let {
                            Timber.e(it)
                            view?.showError("Authorization failed")
                        }
                    }
                }
            }
        }
    }

    // We should do this hack cause api returns URI with parameters after '#'
    // which means that it's uri's fragment (not query which after '?' and before '#')
    // and we can't just use 'Uri.getQuery ()' to parse result
    // ringtest://callback#access_token=vNCTFnBG8PXtg-EhyMpfBTfzELg&token_type=bearer&state=state1506565629103&expires_in=3600&scope=read
    // ringtest://callback#state=state1506565524763&error=access_denied
    private fun parseUri(uri: Uri) = HashMap<String, String?>().apply {
        putAll(extractParams(uri.query))
        putAll(extractParams(uri.fragment))
    }

    private fun extractParams(part: String?) = HashMap<String, String?>().apply {
        part?.let { it.split('&').map { it.split('=') }.forEach { put(it[0], it[1]) } }
    }

    private fun generateState() = "state${System.currentTimeMillis()}"

    override fun restoreState(bundle: Bundle?) {
        bundle?.let { state = it.getString(STATE_KEY) }
    }

    override fun saveInstanceState(bundle: Bundle?) {
        bundle?.putString(STATE_KEY, state)
    }

}