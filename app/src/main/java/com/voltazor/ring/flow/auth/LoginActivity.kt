package com.voltazor.ring.flow.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.voltazor.ring.R
import com.voltazor.ring.base.BaseMvpActivity
import com.voltazor.ring.flow.MainActivity
import com.voltazor.ring.onClick
import com.voltazor.ring.openUrl
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseMvpActivity<ILoginView, ILoginPresenter>(), ILoginView {

    override val presenter = LoginPresenter()

    companion object {

        private const val RESULT_EXPECTED = "result_expected"

        fun newIntent(context: Context, resultExpected: Boolean = false): Intent =
                Intent(context, LoginActivity::class.java).putExtra(RESULT_EXPECTED, resultExpected)

    }

    override val layoutResourceId: Int
        get() = R.layout.activity_login


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        signIn.onClick { presenter.login() }

        skip.onClick { skipLogin() }

        handleToken(intent)
    }

    private fun skipLogin() {
        if (intent.getBooleanExtra(RESULT_EXPECTED, false)) {
            setResult(RESULT_CANCELED)
            finish()
        } else {
            presenter.skipLogin()
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleToken(intent)
    }

    private fun handleToken(intent: Intent?) {
        intent?.data?.let { presenter.handleToken(it) }
    }

    override fun openUrl(url: String) {
        openUrl(url, true)
    }

    override fun navigateMain() {
        if (intent.getBooleanExtra(RESULT_EXPECTED, false)) {
            setResult(RESULT_OK)
            finish()
        } else {
            startActivity(MainActivity.newIntent(this))
            finishAffinity()
        }
    }

}

