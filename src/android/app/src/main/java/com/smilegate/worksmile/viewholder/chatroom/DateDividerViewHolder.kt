package com.smilegate.worksmile.viewholder.chatroom

import android.view.View
import android.widget.TextView
import com.smilegate.worksmile.R
import com.smilegate.worksmile.model.chatroom.DateDivider
import com.smilegate.worksmile.viewholder.chat.MessageViewHolder

class DateDividerViewHolder(view: View) : MessageViewHolder<DateDivider>(view) {
    private val mDateDivider = view.findViewById<TextView>(R.id.tv_divider_date)

    override fun bind(data: DateDivider) {
        mDateDivider.text = data.date
    }
}