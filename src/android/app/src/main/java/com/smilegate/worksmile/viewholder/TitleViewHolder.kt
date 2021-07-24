package com.smilegate.worksmile.viewholder

import android.view.View
import android.widget.TextView
import com.smilegate.worksmile.R
import com.smilegate.worksmile.adapter.listener.ListItemClickListener
import com.smilegate.worksmile.model.Title

class TitleViewHolder(view: View) : BaseViewHolder<Title>(view) {
    var mType: TextView = view.findViewById(R.id.tv_type_chatRoom)

    override fun bind(data: Title, itemClickListener: ListItemClickListener?) {
        mType.text = data.title
    }
}