package com.smilegate.worksmile.adapter.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smilegate.worksmile.R
import com.smilegate.worksmile.model.search.openchat.OpenRoom
import com.smilegate.worksmile.model.search.openchat.OpenRooms
import com.smilegate.worksmile.viewholder.search.SearchOpenChatViewHolder

class SearchOpenChatAdapter : RecyclerView.Adapter<SearchOpenChatViewHolder>() {

    var mOpenRoomList = mutableListOf<OpenRoom>()

    fun setItems(items: OpenRooms) {
        synchronized(mOpenRoomList) {
            mOpenRoomList.run {
                clear()
                addAll(items.openRooms)
            }
        }
        notifyDataSetChanged()
    }

    fun setItem(item: OpenRoom) {
        synchronized(mOpenRoomList) {
            mOpenRoomList.add(item)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchOpenChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_channel, parent, false)
        return SearchOpenChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchOpenChatViewHolder, position: Int) {
        holder.bind(mOpenRoomList[position])
    }

    override fun getItemCount(): Int {
        return mOpenRoomList.size
    }
}