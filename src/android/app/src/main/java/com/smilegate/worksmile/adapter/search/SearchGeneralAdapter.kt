package com.smilegate.worksmile.adapter.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smilegate.worksmile.R
import com.smilegate.worksmile.adapter.listener.SearchGeneralItemClickListener
import com.smilegate.worksmile.model.BaseItem
import com.smilegate.worksmile.model.BaseItemViewType
import com.smilegate.worksmile.viewholder.BaseViewHolder
import com.smilegate.worksmile.viewholder.chatroom.ChatRoomViewHolder
import com.smilegate.worksmile.viewholder.search.SearchUserViewHolder

class SearchGeneralAdapter(private val itemClickListener: SearchGeneralItemClickListener? = null) :
    RecyclerView.Adapter<BaseViewHolder<BaseItem>>() {

    private var mSearchResultList = mutableListOf<BaseItem>()

    fun setItems(items: List<BaseItem>) {
        synchronized(mSearchResultList) {
            mSearchResultList.run {
                clear()
                addAll(items)
            }
            notifyDataSetChanged()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return mSearchResultList[position].getViewType()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<BaseItem> {
        val layoutInflater = LayoutInflater.from(parent.context)

        return when (BaseItemViewType.get(viewType)) {
            BaseItemViewType.USER -> SearchUserViewHolder(
                layoutInflater.inflate(
                    R.layout.item_user,
                    parent,
                    false
                )
            )
            else -> ChatRoomViewHolder(
                layoutInflater.inflate(
                    R.layout.item_chat_room,
                    parent,
                    false
                )
            )
        } as BaseViewHolder<BaseItem>
    }

    override fun onBindViewHolder(holder: BaseViewHolder<BaseItem>, position: Int) {
        holder.bind(mSearchResultList[position], itemClickListener)
    }

    override fun getItemCount(): Int {
        return mSearchResultList.size
    }
}