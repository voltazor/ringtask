package com.voltazor.ring.flow.main

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
import com.voltazor.ring.base.BaseAdapter
import com.voltazor.ring.base.BaseMvpActivity
import com.voltazor.ring.flow.LaunchActivity
import com.voltazor.ring.flow.auth.LoginActivity
import com.voltazor.ring.general.OnItemClickListener
import com.voltazor.ring.general.ItemDecoration
import com.voltazor.ring.general.OnNextPageListener
import com.voltazor.ring.model.Post
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_listing.view.*

/**
 * Created by voltazor on 28/09/17.
 */
class MainActivity : BaseMvpActivity<IMainView, IMainPresenter>(R.layout.activity_main), OnItemClickListener<Post>, IMainView, OnNextPageListener {

    companion object {

        private const val REQUEST_LOGIN = 1488

        fun newIntent(context: Context) = Intent(context, MainActivity::class.java)

    }

    override val presenter = MainPresenter()

    private val adapter: ListingAdapter by lazy { ListingAdapter(this, this, this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary)
        swipeRefresh.setOnRefreshListener { loadListings() }

        list.layoutManager = LinearLayoutManager(this)
        list.addItemDecoration(ItemDecoration(this))
        list.adapter = adapter
        loadListings()
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
            R.id.logout -> presenter.logout()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun login() {
        startActivityForResult(LoginActivity.newIntent(this, true), REQUEST_LOGIN)
    }

    override fun navigateLaunchScreen() {
        startActivity(LaunchActivity.newIntent(this))
        finishAffinity()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_LOGIN && resultCode == RESULT_OK) {
            supportInvalidateOptionsMenu()
            loadListings()
        }
    }

    private fun loadListings() = presenter.loadListings()

    override fun showProgress(enabled: Boolean) {
        swipeRefresh.isRefreshing = enabled
    }

    override fun showListings(posts: List<Post>) = adapter.setList(posts)

    override fun onItemClick(item: Post, view: View, position: Int) {
        if (item.url != null) {
            openUrl(item.url)
        }
    }

    override fun showError(error: String?) {
        showProgress(false)
        super.showError(error)
    }

    override fun showError(errorResId: Int) {
        showProgress(false)
        super.showError(errorResId)
    }

    override fun nexPage() = presenter.nextPage()

    class ListingAdapter(context: Context, listener: OnItemClickListener<Post>, private val nextPageListener: OnNextPageListener) :
            BaseAdapter<Post, ViewHolder>(context, listener) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder.newInstance(parent)

        override fun onBindViewHolder(holder: ViewHolder, item: Post?, position: Int) {
            holder.bind(item)
            if (position >= itemCount - 3) {
                nextPageListener.nexPage()
            }
        }

    }

    class ViewHolder private constructor(view: View?) : RecyclerView.ViewHolder(view) {

        companion object {

            fun newInstance(parent: ViewGroup) = ViewHolder(parent.inflate(R.layout.item_listing))

        }

        fun bind(post: Post?) {
            post?.let {
                itemView.title.text = post.title
                itemView.thumbnail.loadImage(post.thumbnail)
                itemView.comments.text = itemView.context.getQuantityString(R.plurals.comments_format, post.comments)

                val userName = "u/${post.author}"
                val elapsedTime = itemView.context.elapsedTime(post.created * 1000)
                val info = itemView.context.getString(R.string.info_format, userName, elapsedTime)
                itemView.info.text = SpannableString(info).apply {
                    val index = info.indexOf(userName)
                    setSpan(StyleSpan(Typeface.BOLD), index, index + userName.length, 0)
                }
            }
        }

    }

}

