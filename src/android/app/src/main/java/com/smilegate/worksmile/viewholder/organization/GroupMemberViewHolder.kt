package com.smilegate.worksmile.viewholder.organization

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.smilegate.worksmile.R
import com.smilegate.worksmile.model.OrganizationGroup

class GroupMemberViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    var mMemberName: TextView = view.findViewById(R.id.tv_groupName_org)

    fun bind(data: OrganizationGroup) {
        mMemberName.text = data.name
    }
}