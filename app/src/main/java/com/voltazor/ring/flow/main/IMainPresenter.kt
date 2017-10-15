package com.voltazor.ring.flow.main

import com.voltazor.ring.base.IBaseMvpPresenter

/**
 * Created by voltazor on 04/10/17.
 */
interface IMainPresenter: IBaseMvpPresenter<IMainView> {

    fun loadListings()

    fun nextPage()

    fun logout()

}