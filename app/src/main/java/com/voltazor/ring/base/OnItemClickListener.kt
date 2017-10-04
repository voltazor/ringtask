package com.voltazor.ring.base

import android.view.View

/**
 * Created by voltazor on 03/10/17.
 */
interface OnItemClickListener<in T> {

    fun onItemClick(item: T, view: View, position: Int)

}