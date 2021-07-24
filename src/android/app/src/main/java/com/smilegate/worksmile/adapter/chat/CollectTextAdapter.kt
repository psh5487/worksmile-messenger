package com.smilegate.worksmile.adapter.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smilegate.worksmile.R
import com.smilegate.worksmile.model.chat.ChatCollectText
import com.smilegate.worksmile.viewholder.chat.CollectTextViewHolder

class CollectTextAdapter : RecyclerView.Adapter<CollectTextViewHolder>() {

    private var mDataList = mutableListOf<ChatCollectText>()

    fun setItems(items: List<ChatCollectText>) {
        synchronized(mDataList) {
            mDataList.run {
                clear()
                addAll(items)
            }
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectTextViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_collect_text, parent, false
        )
        return CollectTextViewHolder(view)
    }

    override fun onBindViewHolder(holder: CollectTextViewHolder, position: Int) {
        holder.bind(mDataList[position])
    }

    override fun getItemCount(): Int {
        return mDataList.size
    }
}