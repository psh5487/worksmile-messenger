package com.smilegate.worksmile.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.smilegate.worksmile.R
import com.smilegate.worksmile.common.Constants
import com.smilegate.worksmile.common.Constants.EXTRA_INVITED_USERS
import com.smilegate.worksmile.common.Constants.EXTRA_ROOM_ID
import com.smilegate.worksmile.common.Constants.EXTRA_SENDER_ID
import com.smilegate.worksmile.extensions.setVisibility
import com.smilegate.worksmile.model.chat.AttachmentType
import com.smilegate.worksmile.viewmodel.ChatViewModel
import kotlinx.android.synthetic.main.activity_drawer.*
import kotlinx.android.synthetic.main.activity_drawer.view.*
import kotlinx.android.synthetic.main.app_bar_chat.*
import kotlinx.android.synthetic.main.nav_drawer_item.*

class ChatActivity : BaseActivity() {
    private val mViewModel: ChatViewModel by viewModels()
    private var mRoomId: String = ""

    companion object {
        fun createIntent(
            context: Context,
            userId: String,
            roomId: String,
            invitedUsers: Array<String> = emptyArray()
        ): Intent {
            return Intent(context, ChatActivity::class.java)
                .putExtra(EXTRA_SENDER_ID, userId)
                .putExtra(EXTRA_ROOM_ID, roomId)
                .putExtra(EXTRA_INVITED_USERS, invitedUsers)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawer)
        setSupportActionBar(toolbar_chat)

        mRoomId = intent?.getStringExtra(EXTRA_ROOM_ID).orEmpty()

        if (mRoomId.isEmpty()) {
            finish()
        } else {
            initViews()
            initViewModel()
        }
    }

    private fun initViews() {
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        chat_search_input.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                mViewModel.setMessageSearchKeyword(query)

                if (!query.isNullOrBlank()) {
                    chat_search_input.clearFocus()
                }

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        navController.setGraph(R.navigation.mobile_navigation, Bundle().apply {
            putString(EXTRA_SENDER_ID, intent?.getStringExtra(EXTRA_SENDER_ID).orEmpty())
            putString(EXTRA_ROOM_ID, mRoomId)
        })

        navView.setupWithNavController(navController)

        btn_member_chatNav.setOnClickListener {
            closeDrawer {
                startActivity(ChatMemberActivity.createIntent(this, mRoomId))
            }
        }
        btn_invite_chatNav.setOnClickListener {
            closeDrawer {
                startActivityForResult(
                    InviteUserActivity.createIntent(this@ChatActivity, mRoomId),
                    Constants.REQ_INVITE_USERS
                )
            }
        }
        btn_notice_chatNav.setOnClickListener {
            closeDrawer {
                startActivity(
                    ChatAttachInfoActivity.createIntent(
                        this,
                        mRoomId,
                        AttachmentType.NOTIFY
                    )
                )
            }
        }
        btn_link_chatNav.setOnClickListener {
            closeDrawer {
                startActivity(
                    ChatAttachInfoActivity.createIntent(
                        this,
                        mRoomId,
                        AttachmentType.LINK
                    )
                )
            }
        }
        btn_image_chatNav.setOnClickListener {
            closeDrawer {
                startActivity(
                    ChatAttachInfoActivity.createIntent(
                        this,
                        mRoomId,
                        AttachmentType.IMAGE
                    )
                )
            }
        }
        btn_file_chatNav.setOnClickListener {
            closeDrawer {
                startActivity(
                    ChatAttachInfoActivity.createIntent(
                        this,
                        mRoomId,
                        AttachmentType.FILE
                    )
                )
            }
        }

        tv_exit_chatNav.setOnClickListener {
            closeDrawer {
                AlertDialog.Builder(this@ChatActivity)
                    .setMessage("현재 채팅방에서 나가시겠습니까?")
                    .setNegativeButton(R.string.cancel) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .setPositiveButton(R.string.confirm) { dialog, _ ->
                        dialog.dismiss()
                        mViewModel.exitRoom(mRoomId)
                    }
                    .show()
            }
        }
    }

    @SuppressLint("RtlHardcoded")
    private fun closeDrawer(postAction: () -> Unit) {
        drawer_layout.run {
            closeDrawer(Gravity.RIGHT)

            postDelayed({
                postAction.invoke()
            }, 300)
        }
    }

    private fun initViewModel() {
        mViewModel.run {
            roomInfo.observe(this@ChatActivity, {
                supportActionBar?.title = it.roomName
                findViewById<TextView>(R.id.member_count).text = it.memcnt.toString()
            })

            message.observe(this@ChatActivity, {
                showToast(it)
            })

            exitRoom.observe(this@ChatActivity, {
                if (it) {
                    setResult(RESULT_OK)
                    finish()
                }
            })

            messageSearchMode.observe(this@ChatActivity, {
                chat_search_input.setVisibility(it)
            })

            getRoomInfo(mRoomId)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_chat, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.chat_search -> {
                mViewModel.setMessageSearchMode(true)
                true
            }
            R.id.chat_nav -> {
                drawer_layout.openDrawer(GravityCompat.END)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                Constants.REQ_INVITE_USERS -> {
                    data?.getStringArrayExtra(Constants.EXTRA_INVITED_USER_ID_LIST)
                        ?.takeIf { it.isNotEmpty() }?.let {
                            mViewModel.inviteRoomMembers(mRoomId, it.toList())
                        }
                }
                else -> super.onActivityResult(requestCode, resultCode, data)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onBackPressed() {
        if (mViewModel.messageSearchMode.value == true) {
            mViewModel.run {
                chat_search_input.setQuery("", false)
                setMessageSearchMode(false)
                setMessageSearchKeyword(null)
            }
        } else {
            super.onBackPressed()
        }
    }
}