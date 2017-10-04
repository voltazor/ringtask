package com.voltazor.ring.general

import android.content.Context
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View
import com.voltazor.ring.R

/**
 * Created by voltazor on 03/10/17.
 */
class ItemDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private val itemOffset: Int = context.resources.getDimensionPixelSize(R.dimen.margin_small)

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.set(0, 0, 0, 0)
        outRect.bottom = itemOffset
    }

}