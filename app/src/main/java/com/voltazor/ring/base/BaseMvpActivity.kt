package com.voltazor.ring.base

import android.os.Bundle
import android.os.PersistableBundle
import com.voltazor.ring.showToast

/**
 * Created by voltazor on 03/10/17.
 */
abstract class BaseMvpActivity<V : IBaseMvpView, out T : IBaseMvpPresenter<V>>(layoutResourceId: Int) : BaseActivity(layoutResourceId), IBaseMvpView {

    protected abstract val presenter: T

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.attachView(this as V)
        presenter.restoreState(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        super.onSaveInstanceState(outState, outPersistentState)
        presenter.saveInstanceState(outState)
    }

    override fun getContext() = this

    override fun showError(error: String?) = showToast(error)

    override fun showError(errorResId: Int) = showToast(errorResId)

    override fun showMessage(srtResId: Int) = showToast(srtResId)

    override fun showMessage(message: String?) = showToast(message)

    override fun onDestroy() {
        presenter.detachView()
        super.onDestroy()
    }

}