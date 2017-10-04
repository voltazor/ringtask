package com.voltazor.ring

import android.app.Application
import com.voltazor.ring.api.ApiManager
import com.voltazor.ring.general.SharedPrefManager
import timber.log.Timber

/**
 * Created by voltazor on 27/09/17.
 */
class App: Application() {

    companion object {

        val apiManager = ApiManager()

        val spManager = SharedPrefManager()

    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        spManager.init(this)
        apiManager.init()
    }

}