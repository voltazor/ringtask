package com.voltazor.ring.customtabs

import android.content.ComponentName
import android.support.customtabs.CustomTabsClient
import android.support.customtabs.CustomTabsServiceConnection
import java.lang.ref.WeakReference

/**
 * Implementation for the CustomTabsServiceConnection that avoids leaking the
 * ServiceConnectionCallback
 */
class ServiceConnection(connectionCallback: ServiceConnectionCallback) : CustomTabsServiceConnection() {

    // A weak reference to the ServiceConnectionCallback to avoid leaking it.
    private val callback: WeakReference<ServiceConnectionCallback> = WeakReference(connectionCallback)

    override fun onCustomTabsServiceConnected(name: ComponentName, client: CustomTabsClient) {
        callback.get()?.onServiceConnected(client)
    }

    override fun onServiceDisconnected(name: ComponentName) {
        callback.get()?.onServiceDisconnected()
    }
}