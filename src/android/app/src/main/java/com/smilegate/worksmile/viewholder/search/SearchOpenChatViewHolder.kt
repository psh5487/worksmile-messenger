package com.smilegate.worksmile.viewholder.search

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.smilegate.worksmile.R
import com.smilegate.worksmile.model.search.openchat.OpenRoom
import com.squareup.picasso.Picasso

class SearchOpenChatViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var mThumbnail: ImageView = view.findViewById(R.id.iv_thumbnail_channel)
    var mChannelName: TextView = view.findViewById(R.id.tv_name_channel)
    var mMemberCount: TextView = view.findViewById(R.id.tv_cnt_channel)

    @SuppressLint("SetTextI18n")
    fun bind(data: OpenRoom) {
        Picasso.get().load(R.drawable.ic_baseline_people_24)
        mChannelName.text = data.initName
        mMemberCount.text = "${data.memcnt} users"
    }
}