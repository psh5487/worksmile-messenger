package com.smilegate.worksmile.adapter.chatroom

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smilegate.worksmile.R
import com.smilegate.worksmile.adapter.listener.ListItemClickListener
import com.smilegate.worksmile.model.BaseItem
import com.smilegate.worksmile.model.BaseItemViewType
import com.smilegate.worksmile.viewholder.BaseViewHolder
import com.smilegate.worksmile.viewholder.TitleViewHolder
import com.smilegate.worksmile.viewholder.chatroom.ChatRoomViewHolder
import com.smilegate.worksmile.viewholder.search.SearchUserViewHolder

@Suppress("IMPLICIT_CAST_TO_ANY")
class ChatRoomAdapter(
    private val itemClickListener: ListItemClickListener? = null
) : RecyclerView.Adapter<BaseViewHolder<BaseItem>>() {

    private var mChatRoomDataList = mutableListOf<BaseItem>()

    fun setItems(items: List<BaseItem>) {
        synchronized(mChatRoomDataList) {
            mChatRoomDataList.run {
                clear()
                addAll(items)
            }
            notifyDataSetChanged()
        }
    }

    val items: List<BaseItem>
        get() = mChatRoomDataList

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<BaseItem> {
        val layoutInflater = LayoutInflater.from(parent.context)

        return when (BaseItemViewType.get(viewType)) {
            BaseItemViewType.ROOM -> ChatRoomViewHolder(
                layoutInflater.inflate(
                    R.layout.item_chat_room,
                    parent,
                    false
                )
            )
            BaseItemViewType.USER -> SearchUserViewHolder(
                layoutInflater.inflate(
                    R.layout.item_user,
                    parent,
                    false
                )
            )
            else -> TitleViewHolder(
                layoutInflater.inflate(
                    R.layout.item_title,
                    parent,
                    false
                )
            )
        } as BaseViewHolder<BaseItem>
    }

    override fun getItemViewType(position: Int): Int {
        return mChatRoomDataList[position].getViewType()
    }

    override fun getItemCount(): Int {
        return mChatRoomDataList.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder<BaseItem>, position: Int) {
        holder.bind(mChatRoomDataList[position], itemClickListener)
    }
}