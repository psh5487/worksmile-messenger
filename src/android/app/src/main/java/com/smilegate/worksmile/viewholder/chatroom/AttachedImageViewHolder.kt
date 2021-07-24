package com.smilegate.worksmile.viewholder.chatroom

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.smilegate.worksmile.R
import com.smilegate.worksmile.model.chatroom.ChatFile
import com.squareup.picasso.Picasso

class AttachedImageViewHolder(view: View) : AttachmentViewHolder(view) {
    private val mPicasso = Picasso.get()
    private val mThumbnail = view.findViewById<ImageView>(R.id.attached_image).apply {
        val imageWidth = view.context.resources.displayMetrics.widthPixels / 3

        layoutParams = (layoutParams as ViewGroup.LayoutParams).apply {
            width = imageWidth
            height = imageWidth
        }
    }

    override fun bind(file: ChatFile) {
        try {
            mPicasso.load(file.fileUrl).into(mThumbnail)
            mThumbnail.visibility = View.VISIBLE
        } catch (e: Exception) {
            mThumbnail.visibility = View.INVISIBLE
        }
    }
}