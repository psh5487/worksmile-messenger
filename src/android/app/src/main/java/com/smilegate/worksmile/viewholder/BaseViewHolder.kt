package com.smilegate.worksmile.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.smilegate.worksmile.adapter.listener.ListItemClickListener
import com.smilegate.worksmile.model.BaseItem


open class BaseViewHolder<T : BaseItem>(view: View) :
    RecyclerView.ViewHolder(view) {
    open fun bind(data: T) {
    }

    open fun bind(data: T, itemClickListener: ListItemClickListener? = null) {
        bind(data)
    }
}