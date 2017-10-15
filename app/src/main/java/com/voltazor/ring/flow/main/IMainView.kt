package com.voltazor.ring.flow.main

import com.voltazor.ring.base.IBaseMvpView
import com.voltazor.ring.model.Post

/**
 * Created by voltazor on 04/10/17.
 */
interface IMainView: IBaseMvpView {

    fun showListings(posts: List<Post>)

    fun showProgress(enabled: Boolean)

    fun navigateLaunchScreen()

}