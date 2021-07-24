package com.smilegate.worksmile.viewholder.chat

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.smilegate.worksmile.R
import com.smilegate.worksmile.model.chat.ChatCollectText
import kotlinx.android.synthetic.main.item_collect_text.view.*

class CollectTextViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    var mContent: TextView? = view.findViewById(R.id.tv_content_collectText)
    var mInfo: TextView? = view.findViewById(R.id.tv_info_collectText)

    fun bind(data: ChatCollectText) {
        mContent?.text = data.content
        mInfo?.text = data.info
    }
}