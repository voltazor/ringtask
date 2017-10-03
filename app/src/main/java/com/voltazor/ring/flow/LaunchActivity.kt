package com.voltazor.ring.flow

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.voltazor.ring.App.Companion.spManager
import com.voltazor.ring.flow.auth.LoginActivity

/**
 * Created by voltazor on 27/09/17.
 */
class LaunchActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (spManager.token == null) {
            startActivity(LoginActivity.newIntent(this))
        } else {
            startActivity(MainActivity.newIntent(this))
        }
        finish()
    }

}