package com.voltazor.ring.customtabs

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.support.customtabs.CustomTabsIntent
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.util.Log
import com.voltazor.ring.R
import com.voltazor.ring.logTag
import java.util.ArrayList

/**
 * Helper class for Custom Tabs.
 */
object CustomTabsHelper {

    private val STABLE_PACKAGE = "com.android.chrome"
    private val BETA_PACKAGE = "com.chrome.beta"
    private val DEV_PACKAGE = "com.chrome.dev"
    private val LOCAL_PACKAGE = "com.google.android.apps.chrome"
    private val ACTION_CUSTOM_TABS_CONNECTION = "android.support.customtabs.action.CustomTabsService"

    private var packageNameToUse: String? = null

    /**
     * Goes through all apps that handle VIEW intents and have a warmup service. Picks
     * the one chosen by the user if there is one, otherwise makes a best effort to return a
     * valid package name.
     *
     *
     * This is **not** threadsafe.
     *
     * @param context [Context] to use for accessing [PackageManager].
     * @return The package name recommended to use for connecting to custom tabs related components.
     */
    fun getPackageNameToUse(context: Context): String? {
        if (packageNameToUse != null) {
            return packageNameToUse
        }

        val pm = context.packageManager
        // Get default VIEW intent handler.
        val activityIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.example.com"))
        val defaultViewHandlerInfo = pm.resolveActivity(activityIntent, 0)
        var defaultViewHandlerPackageName: String? = null
        if (defaultViewHandlerInfo != null) {
            defaultViewHandlerPackageName = defaultViewHandlerInfo.activityInfo.packageName
        }

        // Get all apps that can handle VIEW intents.
        val resolvedActivityList = pm.queryIntentActivities(activityIntent, 0)
        val packagesSupportingCustomTabs = ArrayList<String>()
        for (info in resolvedActivityList) {
            val serviceIntent = Intent()
            serviceIntent.action = ACTION_CUSTOM_TABS_CONNECTION
            serviceIntent.`package` = info.activityInfo.packageName
            if (pm.resolveService(serviceIntent, 0) != null) {
                packagesSupportingCustomTabs.add(info.activityInfo.packageName)
            }
        }

        // Now packagesSupportingCustomTabs contains all apps that can handle both VIEW intents
        // and service calls.
        if (packagesSupportingCustomTabs.isEmpty()) {
            packageNameToUse = null
        } else if (packagesSupportingCustomTabs.size == 1) {
            packageNameToUse = packagesSupportingCustomTabs[0]
        } else if (!TextUtils.isEmpty(defaultViewHandlerPackageName)
                && !hasSpecializedHandlerIntents(context, activityIntent)
                && packagesSupportingCustomTabs.contains(defaultViewHandlerPackageName)) {
            packageNameToUse = defaultViewHandlerPackageName
        } else if (packagesSupportingCustomTabs.contains(STABLE_PACKAGE)) {
            packageNameToUse = STABLE_PACKAGE
        } else if (packagesSupportingCustomTabs.contains(BETA_PACKAGE)) {
            packageNameToUse = BETA_PACKAGE
        } else if (packagesSupportingCustomTabs.contains(DEV_PACKAGE)) {
            packageNameToUse = DEV_PACKAGE
        } else if (packagesSupportingCustomTabs.contains(LOCAL_PACKAGE)) {
            packageNameToUse = LOCAL_PACKAGE
        }
        return packageNameToUse
    }

    /**
     * Used to check whether there is a specialized handler for a given intent.
     *
     * @param intent The intent to check with.
     * @return Whether there is a specialized handler for the given intent.
     */
    private fun hasSpecializedHandlerIntents(context: Context, intent: Intent): Boolean {
        try {
            val pm = context.packageManager
            val handlers = pm.queryIntentActivities(intent, PackageManager.GET_RESOLVED_FILTER)
            if (handlers == null || handlers.size == 0) {
                return false
            }
            for (resolveInfo in handlers) {
                val filter = resolveInfo.filter ?: continue
                if (filter.countDataAuthorities() == 0 || filter.countDataPaths() == 0) {
                    continue
                }
                if (resolveInfo.activityInfo == null) {
                    continue
                }
                return true
            }
        } catch (e: RuntimeException) {
            Log.e(logTag(), "Runtime exception while getting specialized handlers")
        }

        return false
    }

    fun getCustomTabIntent(context: Context): CustomTabsIntent {
        return CustomTabsIntent.Builder().apply {
            setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary))
            setShowTitle(true)
            enableUrlBarHiding()
        }.build()
    }

}