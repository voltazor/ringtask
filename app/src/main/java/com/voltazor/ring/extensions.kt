package com.voltazor.ring

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.annotation.LayoutRes
import android.support.annotation.StringRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.gson.JsonElement
import com.squareup.picasso.Picasso
import com.voltazor.ring.customtabs.CustomTabActivityHelper.Companion.openCustomTab
import com.voltazor.ring.customtabs.CustomTabActivityHelper.CustomTabFallback
import com.voltazor.ring.customtabs.CustomTabsHelper
import java.util.*
import java.util.Calendar.*
import java.util.concurrent.TimeUnit


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

fun JsonElement?.extractString() = this?.let { if (isJsonNull) null else asString }

fun ViewGroup.inflate(@LayoutRes layoutRes: Int): View = LayoutInflater.from(context).inflate(layoutRes, this, false)

fun ImageView.loadImage(url: String?) {
    Picasso.with(context).load(url).into(this)
}

fun Context.getQuantityString(@StringRes strResId: Int, quantity: Int) : String? = resources.getQuantityString(strResId, quantity, quantity)

fun Context.elapsedTime(time: Long): String {
    val start = getInstance().apply { timeInMillis = time }
    val end = getInstance()

    fun calculateTime(): Pair<Int, Int> {
        val days = TimeUnit.MILLISECONDS.toDays(end.timeInMillis - start.timeInMillis).toInt()
        (days / 365).let {
            if (it > 0) {
                return Pair(it, YEAR)
            }
        }
        (days / 30).let {
            if (it > 0) {
                return Pair(it, MONTH)
            }
        }
        days.let {
            if (it > 0) {
                return Pair(it, DAY_OF_MONTH)
            }
        }
        TimeUnit.MILLISECONDS.toHours(end.timeInMillis - start.timeInMillis).toInt().let {
            if (it > 0) {
                return Pair(it, HOUR)
            }
        }
        return Pair(TimeUnit.MILLISECONDS.toMinutes(end.timeInMillis - start.timeInMillis).toInt(), MINUTE)
    }

    val elapsed = calculateTime()
    return getString(when (elapsed.second) {
        YEAR -> R.string.year_format
        MONTH -> R.string.month_format
        DAY_OF_MONTH -> R.string.day_format
        HOUR -> R.string.hour_format
        else -> R.string.minute_format
    }, elapsed.first)
}