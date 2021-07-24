package com.smilegate.worksmile.viewholder.chat

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.smilegate.worksmile.R
import com.smilegate.worksmile.model.chat.ChatMember
import com.squareup.picasso.Picasso

class ChatMemberViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var mThumbnail: ImageView = view.findViewById(R.id.iv_thumbnail_user)
    var mName: TextView = view.findViewById(R.id.tv_name_user)


    fun bind(data: ChatMember) {
        Picasso.get().load(R.drawable.worksmile_logo).into(mThumbnail)
        mName.text = data.name
    }

}