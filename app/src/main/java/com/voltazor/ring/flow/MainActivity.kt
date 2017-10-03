package com.voltazor.ring.flow

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.voltazor.ring.App
import com.voltazor.ring.R
import com.voltazor.ring.base.BaseActivity
import com.voltazor.ring.onClick
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

/**
 * Created by voltazor on 28/09/17.
 */
class MainActivity : BaseActivity() {

    companion object {

        fun newIntent(context: Context) = Intent(context, MainActivity::class.java)

    }

    override val layoutResourceId: Int
        get() = R.layout.activity_main


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        load.onClick({ loadTop() })
    }

    private fun loadTop() {
        App.apiManager.requestTop().subscribe({
            val text = it.joinToString(separator = "\n") { listing -> """
                    ***
                    ${listing.title}
                    ${listing.thumbnail}
                """.trimIndent() }
            Timber.d(text)
            info.text = text
        }, {
            Timber.e(it, "Error")
        })
    }

}

