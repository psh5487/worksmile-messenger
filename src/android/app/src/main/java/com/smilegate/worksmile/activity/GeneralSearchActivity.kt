package com.smilegate.worksmile.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.smilegate.worksmile.R
import com.smilegate.worksmile.adapter.chatroom.ChatRoomAdapter
import com.smilegate.worksmile.adapter.listener.RoomItemClickListener
import com.smilegate.worksmile.common.Constants
import com.smilegate.worksmile.common.PreferenceUtil
import com.smilegate.worksmile.model.BaseItem
import com.smilegate.worksmile.model.Title
import com.smilegate.worksmile.model.chatroom.ChatRoom
import com.smilegate.worksmile.network.ApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_general_search.*
import kotlinx.android.synthetic.main.activity_openchat_search.*

class GeneralSearchActivity : BaseActivity() {

    private val mUserId = PreferenceUtil.userId

    private val mAdapter: ChatRoomAdapter by lazy {
        ChatRoomAdapter(object : RoomItemClickListener {
            override fun onRoomClicked(item: ChatRoom) {
                val chatIntent = Intent(this@GeneralSearchActivity, ChatActivity::class.java)
                    .putExtra(Constants.EXTRA_SENDER_ID, mUserId)
                    .putExtra(Constants.EXTRA_ROOM_ID, item.roomId)
                startActivity(chatIntent)
            }

            override fun onRoomLongClicked(position: Int, item: ChatRoom) {
                Toast.makeText(
                    this@GeneralSearchActivity,
                    item.favoriteType,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_general_search)

        initView()
    }

    @SuppressLint("CheckResult")
    fun load(userId: String, input: String) {
        ApiService.searchGeneralApi.getSearchResults(userId, input)
            .map { resp ->
                val data = resp.getDataOrThrowable()
                val items = mutableListOf<BaseItem>()
                items.run {
                    add(Title("채팅방"))
                    addAll(data.rooms)
                    add(Title("사용자"))
                    addAll(data.users)
                }
                return@map items
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                mAdapter.setItems(it)
            }, {
                Log.d(TAG, it.toString())
            })
    }

    private fun initView() {
        rv_search_general.run {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)
        }
        search_general.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                load(mUserId, query)
                return true
            }

            override fun onQueryTextChange(newQuery: String?): Boolean {
                return false
            }
        })
    }
}