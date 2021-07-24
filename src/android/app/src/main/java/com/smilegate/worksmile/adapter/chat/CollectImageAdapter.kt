package com.smilegate.worksmile.adapter.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smilegate.worksmile.R
import com.smilegate.worksmile.model.chat.ChatCollectImage
import com.smilegate.worksmile.viewholder.chat.CollectImageViewHolder

class CollectImageAdapter : RecyclerView.Adapter<CollectImageViewHolder>() {
    private var mImageList = mutableListOf<ChatCollectImage>()

    fun setItems(items: List<ChatCollectImage>) {
        synchronized(mImageList) {
            mImageList.run {
                clear()
                addAll(items)
            }
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectImageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_collect_image, parent, false)

        return CollectImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: CollectImageViewHolder, position: Int) {
        holder.bind(mImageList[position])
    }

    override fun getItemCount(): Int {
        return mImageList.size
    }
}