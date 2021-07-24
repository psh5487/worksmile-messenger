package com.smilegate.worksmile.adapter.chatroom

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smilegate.worksmile.R
import com.smilegate.worksmile.model.chat.AttachmentType
import com.smilegate.worksmile.model.chatroom.ChatFile
import com.smilegate.worksmile.viewholder.chatroom.AttachedImageViewHolder
import com.smilegate.worksmile.viewholder.chatroom.AttachmentViewHolder

class AttachmentAdapter : RecyclerView.Adapter<AttachmentViewHolder>() {
    private val mAttachmentList = mutableListOf<ChatFile>()

    fun setItems(attachments: List<ChatFile>) {
        synchronized(mAttachmentList) {
            mAttachmentList.run {
                clear()
                addAll(attachments)
            }
        }

        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return AttachmentType.type(mAttachmentList[position].type).viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttachmentViewHolder {
        return AttachedImageViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_attached_image, parent, false)
        )
    }

    override fun onBindViewHolder(holder: AttachmentViewHolder, position: Int) {
        holder.bind(mAttachmentList[position])
    }

    override fun getItemCount(): Int {
        return mAttachmentList.size
    }
}