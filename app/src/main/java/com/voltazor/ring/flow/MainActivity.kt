package com.voltazor.ring.flow

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.voltazor.ring.*
import com.voltazor.ring.base.BaseActivity
import com.voltazor.ring.base.BaseAdapter
import com.voltazor.ring.base.OnItemClickListener
import com.voltazor.ring.flow.auth.LoginActivity
import com.voltazor.ring.general.ItemDecoration
import com.voltazor.ring.model.Listing
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_listing.view.*
import timber.log.Timber

/**
 * Created by voltazor on 28/09/17.
 */
class MainActivity : BaseActivity(), OnItemClickListener<Listing> {

    companion object {

        private const val REQUEST_LOGIN = 231

        fun newIntent(context: Context) = Intent(context, MainActivity::class.java)

    }

    override val layoutResourceId: Int
        get() = R.layout.activity_main


    private val adapter: ListingAdapter = lazy { ListingAdapter(this, this) }.value

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary)
        swipeRefresh.setOnRefreshListener { loadTop() }

        list.layoutManager = LinearLayoutManager(this)
        list.addItemDecoration(ItemDecoration(this))
        list.adapter = adapter
        loadTop()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (App.spManager.isAnonymous) {
            menuInflater.inflate(R.menu.menu_login, menu)
        } else {
            menuInflater.inflate(R.menu.menu_logout, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.login -> login()
            R.id.logout -> logout()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun login() {
        startActivityForResult(LoginActivity.newIntent(this, true), REQUEST_LOGIN)
    }

    private fun logout() {
        App.spManager.clearAuth()
        startActivity(LaunchActivity.newIntent(this))
        finishAffinity()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_LOGIN && resultCode == RESULT_OK) {
            loadTop()
        }
    }

    private fun loadTop() {
        App.apiManager.requestTop().subscribe({
            showListing(it)
        }, {
            Timber.e(it, "Error")
        })
    }

    private fun showListing(listings: List<Listing>) {
        swipeRefresh.isRefreshing = false
        adapter.setList(listings)
    }

    override fun onItemClick(item: Listing, view: View, position: Int) {
        showToast(item.id)
    }

    class ListingAdapter(context: Context, listener: OnItemClickListener<Listing>) : BaseAdapter<Listing, ViewHolder>(context, listener) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder.newInstance(parent)

        override fun onBindViewHolder(holder: ViewHolder, item: Listing?, position: Int) = holder.bind(item)

    }

    class ViewHolder private constructor(view: View?) : RecyclerView.ViewHolder(view) {

        companion object {

            fun newInstance(parent: ViewGroup) = ViewHolder(parent.inflate(R.layout.item_listing))

        }

        fun bind(listing: Listing?) {
            listing?.let {
                itemView.title.text = listing.title
                itemView.thumbnail.loadImage(listing.thumbnail)
                itemView.comments.text = itemView.context.getQuantityString(R.plurals.comments_format, listing.comments)

                val userName = "u/${listing.author}"
                val elapsedTime = itemView.context.elapsedTime(listing.created * 1000)
                val info = itemView.context.getString(R.string.info_format, userName, elapsedTime)
                itemView.info.text = SpannableString(info).apply {
                    val index = info.indexOf(userName)
                    setSpan(StyleSpan(Typeface.BOLD), index, index + userName.length, 0)
                }
            }
        }

    }


}

