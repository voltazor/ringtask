package com.voltazor.ring.base

import android.content.Context
import android.support.annotation.StringRes
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.voltazor.ring.general.OnItemClickListener
import com.voltazor.ring.onClick
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Created by voltazor on 03/10/17.
 */
abstract class BaseAdapter<T, VH : RecyclerView.ViewHolder>(protected val context: Context, list: List<T>? = null,
                                                            private val itemClickListener: OnItemClickListener<T>? = null) :
        RecyclerView.Adapter<VH>() {

    constructor(context: Context): this(context, null, null)

    constructor(context: Context, listener: OnItemClickListener<T>?): this(context, null, listener)

    private val list = if (list == null) CopyOnWriteArrayList() else CopyOnWriteArrayList(list)

    abstract override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH

    fun getItem(position: Int) = if (position < 0 || position >= list.size) null else list[position]

    override fun onBindViewHolder(holder: VH, position: Int) {
        onBindViewHolder(holder, getItem(holder.adapterPosition), position)
        holder.itemView.onClick { getItem(position)?.let { itemClickListener?.onItemClick(it, holder.itemView, holder.adapterPosition) } }
    }

    protected fun getString(@StringRes strResId: Int) = context.getString(strResId)

    protected fun getString(@StringRes resId: Int, vararg formatArgs: Any) = context.getString(resId, *formatArgs)

    abstract fun onBindViewHolder(holder: VH, item: T?, position: Int)

    override fun getItemCount() = list.size

    fun getList() = list

    fun addItem(item: T) {
        if (list.add(item)) {
            notifyDataSetChanged()
        }
    }

    fun addItem(item: T, position: Int) {
        list.add(position, item)
        notifyDataSetChanged()
    }

    fun setList(list: List<T>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    fun removeItem(item: T) {
        if (list.remove(item)) {
            notifyDataSetChanged()
        }
    }

    fun removeItem(position: Int) {
        if (list.removeAt(position) != null) {
            notifyDataSetChanged()
        }
    }

    fun clear() {
        list.clear()
        notifyDataSetChanged()
    }

}