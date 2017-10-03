package com.voltazor.ring.customtabs

import android.app.Service
import android.content.Intent
import android.os.Binder

/**
 * Empty service used by the custom tab to bind to, raising the application's importance.
 */
class KeepAliveService : Service() {

    companion object {

        private val sBinder = Binder()

    }

    override fun onBind(intent: Intent) = sBinder

}