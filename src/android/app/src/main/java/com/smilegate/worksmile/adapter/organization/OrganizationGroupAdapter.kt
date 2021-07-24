package com.smilegate.worksmile.adapter.organization

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smilegate.worksmile.R
import com.smilegate.worksmile.model.OrganizationGroup
import com.smilegate.worksmile.viewholder.organization.OrganizationGroupViewHolder

class OrganizationGroupAdapter : RecyclerView.Adapter<OrganizationGroupViewHolder>() {

    private val mGroupList = mutableListOf<OrganizationGroup>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrganizationGroupViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_organization_group, parent, false)

        return OrganizationGroupViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrganizationGroupViewHolder, position: Int) {
        holder.bind(mGroupList[position])
    }

    override fun getItemCount(): Int {
        return mGroupList.size
    }

    fun setItem(item: OrganizationGroup, position: Int) {
        synchronized(mGroupList) {
            mGroupList.add(position, item)
        }

        notifyDataSetChanged()
    }

    fun setItems(items: List<OrganizationGroup>) {
        synchronized(mGroupList) {
            mGroupList.run {
                clear()
                addAll(items)
            }
        }

        notifyDataSetChanged()
    }
}