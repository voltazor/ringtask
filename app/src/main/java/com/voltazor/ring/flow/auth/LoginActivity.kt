package com.voltazor.ring.flow.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.voltazor.ring.flow.MainActivity
import com.voltazor.ring.R
import com.voltazor.ring.base.BaseMvpActivity
import com.voltazor.ring.openUrl
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseMvpActivity<ILoginView, ILoginPresenter>(), ILoginView {

    override val presenter = LoginPresenter()

    companion object {

        fun newIntent(context: Context): Intent = Intent(context, LoginActivity::class.java)

    }

    override val layoutResourceId: Int
        get() = R.layout.activity_login


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        signIn.setOnClickListener { presenter.login() }

        handleToken(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleToken(intent)
    }

    private fun handleToken(intent: Intent?) {
        intent?.let { presenter.handleToken(intent.data) }
    }

    override fun openUrl(url: String) {
        openUrl(url, true)
    }

    override fun loginSucceed() {
        startActivity(MainActivity.newIntent(this))
        finishAffinity()
    }

}

