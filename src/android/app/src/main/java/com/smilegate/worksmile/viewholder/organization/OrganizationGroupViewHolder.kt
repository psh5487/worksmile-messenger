package com.smilegate.worksmile.viewholder.organization

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.smilegate.worksmile.R
import com.smilegate.worksmile.model.OrganizationGroup

class OrganizationGroupViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    var mGroupName: TextView = view.findViewById(R.id.tv_groupName_org)

    fun bind(data: OrganizationGroup) {
        mGroupName.text = data.name
    }
}