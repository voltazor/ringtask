package com.voltazor.ring

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.annotation.StringRes
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.gson.JsonElement
import com.voltazor.ring.customtabs.CustomTabActivityHelper.CustomTabFallback
import com.voltazor.ring.customtabs.CustomTabActivityHelper.Companion.openCustomTab
import com.voltazor.ring.customtabs.CustomTabsHelper
import java.util.*

/**
 * Created by voltazor on 28/09/17.
 */
fun TextView.textString() = text.toString()

fun <T : Comparable<T>?> List<T>.sort(): List<T> {
    Collections.sort(this)
    return this
}

fun Context.showToast(@StringRes strResId: Int, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, strResId, length).show()
}

fun Context.showToast(text: String?, length: Int = Toast.LENGTH_SHORT) {
    text?.let { Toast.makeText(this, text, length).show() }
}

fun Any.logTag() = javaClass.simpleName!!

fun Activity.openUrl(url: String, noHistory: Boolean = false) = openUrl(Uri.parse(url), noHistory)

fun Activity.openUrl(uri: Uri, noHistory: Boolean = false) = try {
    val intent = CustomTabsHelper.getCustomTabIntent(this)
    openCustomTab(this, intent, uri, object : CustomTabFallback {
        override fun openUri(activity: Activity, uri: Uri) {
            activity.startActivity(Intent(Intent.ACTION_VIEW, uri))
        }
    }, noHistory)
    true
} catch (ignored: ActivityNotFoundException) {
    false
}

fun View.onClick(f: () -> Unit) {
    setOnClickListener({ f.invoke() })
}

fun JsonElement?.extractString() = this?.let { if (isJsonNull) null else asString  }