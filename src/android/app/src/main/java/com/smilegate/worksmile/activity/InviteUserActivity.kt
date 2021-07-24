package com.smilegate.worksmile.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.smilegate.worksmile.R
import com.smilegate.worksmile.adapter.chatroom.ChatRoomAdapter
import com.smilegate.worksmile.adapter.listener.UserClickListener
import com.smilegate.worksmile.common.Constants
import com.smilegate.worksmile.common.PreferenceUtil
import com.smilegate.worksmile.model.User
import com.smilegate.worksmile.viewmodel.InviteUserViewModel
import kotlinx.android.synthetic.main.activity_invite_user.*
import kotlinx.android.synthetic.main.item_select.view.*


class InviteUserActivity : AppCompatActivity() {
    private var mRoomId: String = ""
    private val mInviteUserViewModel: InviteUserViewModel by viewModels()

    private val mAdapter by lazy {
        ChatRoomAdapter(object : UserClickListener {
            override fun onUserClicked(user: User) {
                mInviteUserViewModel.addInvitedUser(user)
            }
        })
    }

    companion object {
        fun createIntent(context: Context, roomId: String? = null): Intent {
            return Intent(context, InviteUserActivity::class.java).apply {
                roomId?.let {
                    putExtra(Constants.EXTRA_ROOM_ID, it)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invite_user)

        mRoomId = intent?.getStringExtra(Constants.EXTRA_ROOM_ID).orEmpty()

        initView()
        initViewModel()
    }

    private fun initView() {
        rv_search_invite.adapter = mAdapter

        search_invite.run {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    mInviteUserViewModel.setKeyword(query.orEmpty())
                    return !query.isNullOrBlank()
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    mInviteUserViewModel.setKeyword(newText.orEmpty())
                    return !newText.isNullOrBlank()
                }
            })

            requestFocus()
        }

        create_room_cta.run {
            create_room_cta.text = if (mRoomId.isNotBlank()) "초대하기" else "채팅방 생성하기"

            setOnClickListener {
                if (mRoomId.isBlank()) {
                    mInviteUserViewModel.createChatRoom()
                } else {
                    setResult(
                        RESULT_OK, Intent().putExtra(
                            Constants.EXTRA_INVITED_USER_ID_LIST,
                            mInviteUserViewModel.invitedUserSet.value?.map { it.uid }
                                ?.toTypedArray()
                        )
                    )

                    finish()
                }
            }
        }
    }

    private fun initViewModel() {
        mInviteUserViewModel.run {
            userList.observe(this@InviteUserActivity, {
                mAdapter.setItems(it)
            })

            createChatRoomEnabled.observe(this@InviteUserActivity, { isEnabled ->
                (if (isEnabled) View.VISIBLE else View.GONE).let {
                    sv_select_invite.visibility = it
                    create_room_cta.visibility = it
                }
            })

            invitedUserSet.observe(this@InviteUserActivity, {
                val childCount = ll_select_invite.childCount

                for (index in 0..childCount) {
                    ll_select_invite.getChildAt(index)?.visibility = View.GONE
                }

                it.forEachIndexed { index, user ->
                    ((ll_select_invite.getChildAt(index) as? SelectedUserView) ?: SelectedUserView(
                        this@InviteUserActivity
                    ).apply {
                        ll_select_invite.addView(this, index)
                    }).run {
                        visibility = View.VISIBLE

                        itemClickListener = object : SelectedUserView.ItemClickListener {
                            override fun onDeleteUserClicked(user: User) {
                                mInviteUserViewModel.deleteInvitedUser(user)
                            }
                        }

                        setUser(user)
                    }
                }
            })

            createdRoom.observe(this@InviteUserActivity, { pair ->
                val roomId = pair.first.roomId

                if (roomId.isNotBlank()) {
                    startActivity(
                        ChatActivity.createIntent(
                            this@InviteUserActivity, mUserId, roomId,
                            pair.second.filter { it != PreferenceUtil.userId }.toTypedArray()
                        )
                    )

                    setResult(RESULT_OK)
                    finish()
                }
            })

            message.observe(this@InviteUserActivity, {
                Toast.makeText(this@InviteUserActivity, it, Toast.LENGTH_SHORT)
                    .show()
            })
        }
    }

    private class SelectedUserView(context: Context, attrs: AttributeSet? = null) :
        FrameLayout(context, attrs) {
        var itemClickListener: ItemClickListener? = null

        interface ItemClickListener {
            fun onDeleteUserClicked(user: User)
        }

        init {
            LayoutInflater.from(context).inflate(R.layout.item_select, this, true)
        }

        fun setUser(user: User) {
            tv_name_user.text = user.uname

            iv_delete_user.setOnClickListener {
                itemClickListener?.onDeleteUserClicked(user)
            }
        }
    }
}