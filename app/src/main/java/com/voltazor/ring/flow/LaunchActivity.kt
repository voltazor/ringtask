package com.voltazor.ring.flow

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.voltazor.ring.App.Companion.spManager
import com.voltazor.ring.flow.auth.LoginActivity
import com.voltazor.ring.flow.main.MainActivity

/**
 * Created by voltazor on 27/09/17.
 */
class LaunchActivity: AppCompatActivity() {

    companion object {

        fun newIntent(context: Context) = Intent(context, LaunchActivity::class.java)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!spManager.isAnonymous && spManager.token == null) {
            startActivity(LoginActivity.newIntent(this))
        } else {
            startActivity(MainActivity.newIntent(this))
        }
        finish()
    }

}