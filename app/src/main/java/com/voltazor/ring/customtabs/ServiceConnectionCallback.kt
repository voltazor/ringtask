package com.voltazor.ring.customtabs

import android.support.customtabs.CustomTabsClient

/**
 * Callback for events when connecting and disconnecting from Custom Tabs Service.
 */
interface ServiceConnectionCallback {

    /**
     * Called when the service is connected.
     *
     * @param client a CustomTabsClient
     */
    fun onServiceConnected(client: CustomTabsClient)

    /**
     * Called when the service is disconnected.
     */
    fun onServiceDisconnected()

}