package com.voltazor.ring.flow.main

import com.voltazor.ring.App
import com.voltazor.ring.R
import com.voltazor.ring.base.BaseMvpPresenterImpl
import com.voltazor.ring.model.Post
import timber.log.Timber

/**
 * Created by voltazor on 04/10/17.
 */
class MainPresenter : BaseMvpPresenterImpl<IMainView>(), IMainPresenter {

    private var posts: MutableList<Post> = mutableListOf()

    private var after: String? = null
    private var loading = false

    override fun loadListings() {
        after = null
        posts.clear()
        loadListing(after)
    }

    override fun nextPage() {
        after?.let { loadListing(after) }
    }

    private fun loadListing(after: String?) {
        if (!loading) {
            loading = true
            view?.showProgress(true)
            addSubscription(apiManager.requestTop(after, posts.size).subscribe({ listingData ->
                posts.addAll(listingData.children)
                this.after = listingData.after
                view?.showListings(posts)
                view?.showProgress(false)
                loading = false
            }, {
                Timber.e(it, "loadListings: $after failed")
                view?.showError(getString(R.string.request_failed))
                view?.showProgress(false)
                loading = false
            }))
        }
    }

    override fun logout() {
        App.spManager.clearAuth()
        view?.navigateLaunchScreen()
    }

}