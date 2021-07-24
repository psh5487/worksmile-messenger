package com.smilegate.worksmile.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import com.smilegate.worksmile.R
import com.smilegate.worksmile.activity.*
import com.smilegate.worksmile.adapter.chatroom.ChatRoomAdapter
import com.smilegate.worksmile.adapter.listener.RoomItemClickListener
import com.smilegate.worksmile.common.Constants
import com.smilegate.worksmile.common.PreferenceUtil
import com.smilegate.worksmile.model.chatroom.ChatRoom
import com.smilegate.worksmile.viewmodel.RoomListViewModel
import kotlinx.android.synthetic.main.fragment_room_list.*

class RoomListFragment : BaseFragment() {
    private val mViewModel: RoomListViewModel by viewModels()
    var mUserId = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mUserId = PreferenceUtil.userId
    }

    private val mAdapter: ChatRoomAdapter by lazy {
        ChatRoomAdapter(object : RoomItemClickListener {
            override fun onRoomClicked(item: ChatRoom) {
                startActivityForResult(
                    ChatActivity.createIntent(requireContext(), mUserId, item.roomId),
                    Constants.REQ_ROOM
                )
            }

            override fun onRoomLongClicked(position: Int, item: ChatRoom) {
                RoomOptionDialog.newInstance(item).apply {
                    itemClickListener = object : RoomOptionDialog.ItemClickListener {
                        override fun onSetFavoriteClicked(roomId: String, isFavorite: Boolean) {

                        }

                        override fun onDeleteRoomClicked(roomId: String) {
                            AlertDialog.Builder(requireContext())
                                .setMessage("${item.roomName}을 삭제하시겠습니까?")
                                .setNegativeButton(R.string.cancel) { dialog, _ ->
                                    dialog.dismiss()
                                }
                                .setPositiveButton(R.string.confirm) { dialog, _ ->
                                    dialog.dismiss()
                                    mViewModel.deleteRoom(roomId)
                                }
                                .show()
                        }

                        override fun onExitRoomClicked(roomId: String) {
                            AlertDialog.Builder(requireContext())
                                .setMessage("${item.roomName}에서 나가시겠습니까?")
                                .setNegativeButton(R.string.cancel) { dialog, _ ->
                                    dialog.dismiss()
                                }
                                .setPositiveButton(R.string.confirm) { dialog, _ ->
                                    dialog.dismiss()
                                    mViewModel.exitRoom(roomId)
                                }
                                .show()
                        }
                    }
                }.show(childFragmentManager, "")
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_room_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initViewModel()
        getRooms()
    }

    private fun initView() {
        roomList.adapter = mAdapter
        val items = arrayOf("비공개 채팅방", "공개 채팅방")

        fab_main.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())

            builder.run {
                setTitle("채팅방 생성")
                setItems(items) { _, position ->
                    when (position) {
                        0 -> {
                            showToast(position.toString())
                        }
                        1 -> {
                            showToast(position.toString())
                        }
                    }
                }
                show()
            }

            startActivityForResult(
                Intent(context, InviteUserActivity::class.java),
                Constants.REQ_INVITE_USERS
            )
        }
    }

    private fun initViewModel() {
        mViewModel.run {
            startSenderSub(mUserId)

            userMessage.observe(viewLifecycleOwner, {
                getRooms()
            })

            message.observe(viewLifecycleOwner, {
                showToast(it)
            })

            roomList.observe(viewLifecycleOwner, {
                mAdapter.setItems(it)
            })
        }
    }

    @SuppressLint("CheckResult")
    fun getRooms() {
        mViewModel.getRooms()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                Constants.REQ_ROOM, Constants.REQ_INVITE_USERS -> getRooms()
                else -> super.onActivityResult(requestCode, resultCode, data)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}