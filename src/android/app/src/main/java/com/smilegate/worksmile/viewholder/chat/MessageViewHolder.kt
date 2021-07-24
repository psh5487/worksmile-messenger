package com.smilegate.worksmile.viewholder.chat

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.BackgroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import android.webkit.URLUtil
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.smilegate.worksmile.R
import com.smilegate.worksmile.adapter.listener.MessageClickListener
import com.smilegate.worksmile.extensions.setVisibility
import com.smilegate.worksmile.model.chatroom.ChatRoomItem
import com.squareup.picasso.Picasso

open class MessageViewHolder<T : ChatRoomItem>(view: View) : RecyclerView.ViewHolder(view) {
    protected val mPicasso = Picasso.get()

    open fun bind(data: T) {
    }

    open fun bind(data: T, itemClickListener: MessageClickListener?) {
        bind(data)
    }

    fun setImage(url: String?, imageView: ImageView): Boolean {
        return if (URLUtil.isValidUrl(url.orEmpty())) {
            try {
                imageView.run {
                    mPicasso.load(url).into(this)
                    setVisibility(true)
                }

                true
            } catch (e: Exception) {
                imageView.setVisibility(false)
                false
            }
        } else {
            imageView.setVisibility(false)
            false
        }
    }

    protected fun setMessage(textView: TextView, message: String, keyword: String) {
        textView.run {
            text = message.indexOf(keyword, 0, true)
                .takeIf { it >= 0 && keyword.isNotBlank() }
                ?.let { startIndex ->
                    SpannableString(message).apply {
                        setSpan(
                            BackgroundColorSpan(
                                ContextCompat.getColor(
                                    context,
                                    R.color.sub_red
                                )
                            ),
                            startIndex,
                            keyword.length,
                            Spannable.SPAN_INCLUSIVE_INCLUSIVE
                        )
                        setSpan(
                            StyleSpan(Typeface.BOLD),
                            startIndex,
                            keyword.length,
                            Spannable.SPAN_INCLUSIVE_INCLUSIVE
                        )
                    }
                } ?: message
        }
    }
}