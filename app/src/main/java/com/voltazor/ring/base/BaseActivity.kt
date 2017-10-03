package com.voltazor.ring.base

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v7.widget.Toolbar
import com.trello.navi.component.support.NaviAppCompatActivity
import com.voltazor.ring.R

/**
 * Created by voltazor on 27/09/17.
 */
abstract class BaseActivity: NaviAppCompatActivity() {

    @get:LayoutRes
    protected abstract val layoutResourceId: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (layoutResourceId != 0) {
            setContentView(layoutResourceId)
        }
        setSupportActionBar(findViewById(R.id.toolbar) as Toolbar?)
    }

}