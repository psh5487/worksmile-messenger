package com.smilegate.worksmile.viewholder.chat

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.smilegate.worksmile.R
import com.smilegate.worksmile.model.chat.ChatCollectImage
import com.smilegate.worksmile.model.chat.ChatCollectText
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_collect_text.view.*

class CollectImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    var mContent: ImageView = view.findViewById(R.id.iv_content_collectImage)

    fun bind(data: ChatCollectImage) {
        Picasso.get().load(R.drawable.worksmile_logo).into(mContent)
        Log.d("SYH",data.content)
    }
}