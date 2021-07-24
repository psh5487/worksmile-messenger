package com.smilegate.worksmile.adapter.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smilegate.worksmile.R
import com.smilegate.worksmile.model.chat.ChatMember
import com.smilegate.worksmile.viewholder.chat.ChatMemberViewHolder

class ChatMemberAdapter : RecyclerView.Adapter<ChatMemberViewHolder>() {

    private var mMemberList = mutableListOf<ChatMember>()

    fun setItems(items: List<ChatMember>) {
        synchronized(mMemberList) {
            mMemberList.run {
                clear()
                addAll(items)
            }
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatMemberViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user, parent, false)

        return ChatMemberViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatMemberViewHolder, position: Int) {
        holder.bind(mMemberList[position])
    }

    override fun getItemCount(): Int {
        return mMemberList.size
    }
}