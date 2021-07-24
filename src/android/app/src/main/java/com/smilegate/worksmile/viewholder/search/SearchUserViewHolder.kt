package com.smilegate.worksmile.viewholder.search

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.smilegate.worksmile.R
import com.smilegate.worksmile.adapter.listener.ListItemClickListener
import com.smilegate.worksmile.adapter.listener.UserClickListener
import com.smilegate.worksmile.common.CircleTransform
import com.smilegate.worksmile.model.User
import com.smilegate.worksmile.viewholder.BaseViewHolder
import com.squareup.picasso.Picasso

class SearchUserViewHolder(view: View) : BaseViewHolder<User>(view) {
    private val mPicasso = Picasso.get()

    private val mThumbnail: ImageView = view.findViewById(R.id.iv_thumbnail_user)
    private val mName: TextView = view.findViewById(R.id.tv_name_user)

    override fun bind(data: User, itemClickListener: ListItemClickListener?) {
        mPicasso.load("https://s3-ap-northeast-1.amazonaws.com/jobjava/IMAGE_admin_210203_200335_user2.png")
            .transform(CircleTransform()).into(mThumbnail)
        mName.text = data.uname

        itemView.setOnClickListener {
            (itemClickListener as? UserClickListener)?.onUserClicked(data)
        }
    }
}