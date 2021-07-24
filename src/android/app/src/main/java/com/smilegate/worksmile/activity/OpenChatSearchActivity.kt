package com.smilegate.worksmile.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.smilegate.worksmile.R
import com.smilegate.worksmile.adapter.search.SearchOpenChatAdapter
import com.smilegate.worksmile.common.PreferenceUtil
import com.smilegate.worksmile.network.ApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_openchat_search.*

class OpenChatSearchActivity : BaseActivity() {

    private val mAdapter = SearchOpenChatAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_openchat_search)

        initView()
    }

    @SuppressLint("CheckResult")
    fun load(input: String?) {
        ApiService.searchOpenChatApi.getChannelList(PreferenceUtil.userId, input ?: "")
            .map {
                it.getDataOrThrowable()
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                mAdapter.setItems(it)
            }, {
                throwError(it)
            })
    }

    private fun initView() {
        rv_search_openChat.run {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)
        }
        search_openChat.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                load(query)
                return true
            }

            override fun onQueryTextChange(newQuery: String?): Boolean {
                return false
            }

        })
    }
}