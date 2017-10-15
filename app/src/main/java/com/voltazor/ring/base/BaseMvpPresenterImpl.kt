package com.voltazor.ring.base

import android.os.Bundle
import android.support.annotation.StringRes
import com.trello.navi.Event
import com.trello.navi.NaviComponent
import com.trello.navi.rx.RxNavi
import com.voltazor.ring.App
import rx.Subscription
import rx.internal.util.SubscriptionList

/**
 * Created by voltazor on 03/10/17.
 */
open class BaseMvpPresenterImpl<V> : IBaseMvpPresenter<V> where V : IBaseMvpView, V : NaviComponent {

    protected var view: V? = null

    protected val apiManager = App.apiManager
    protected val spManager = App.spManager

    private val subscriptionList = SubscriptionList()

    override fun attachView(view: V) {
        this.view = view
        subscriptionList.add(RxNavi.observe(view, Event.DESTROY).subscribe { detachView() })
    }

    override fun restoreState(bundle: Bundle?) {
    }

    override fun saveInstanceState(bundle: Bundle?) {
    }

    protected fun addSubscription(subscription: Subscription) = subscriptionList.add(subscription)

    protected fun getString(@StringRes strResId: Int) = view?.getContext()?.getString(strResId)

    protected fun getString(@StringRes strResId: Int, vararg formatArgs: Any) = view?.getContext()?.getString(strResId, formatArgs)

    override fun detachView() {
        subscriptionList.unsubscribe()
        subscriptionList.clear()
        view = null
    }

}