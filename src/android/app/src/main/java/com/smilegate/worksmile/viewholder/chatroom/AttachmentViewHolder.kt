package com.smilegate.worksmile.viewholder.chatroom

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.smilegate.worksmile.model.chatroom.ChatFile

abstract class AttachmentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun bind(file: ChatFile)
}