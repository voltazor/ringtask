package com.voltazor.ring.base

import android.os.Bundle
import android.support.v7.widget.Toolbar
import com.trello.navi.component.support.NaviAppCompatActivity
import com.voltazor.ring.R

/**
 * Created by voltazor on 27/09/17.
 */
abstract class BaseActivity(private val layoutResourceId: Int): NaviAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (layoutResourceId != 0) {
            setContentView(layoutResourceId)
        }
        setSupportActionBar(findViewById(R.id.toolbar) as Toolbar?)
    }

}