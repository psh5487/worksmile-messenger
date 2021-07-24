package com.smilegate.worksmile.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.smilegate.worksmile.R
import com.smilegate.worksmile.adapter.organization.OrganizationGroupAdapter
import com.smilegate.worksmile.model.OrganizationGroup
import kotlinx.android.synthetic.main.fragment_organization.*

class OrganizationFragment : Fragment() {

    private val mAdapter: OrganizationGroupAdapter = OrganizationGroupAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_organization, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_groups_org.run {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }
        val dataList = arrayListOf<OrganizationGroup>(
            OrganizationGroup("JobJava1"),
            OrganizationGroup("JobJava2"),
            OrganizationGroup("JobJava3")
        )
        mAdapter.setItems(dataList)
    }
}