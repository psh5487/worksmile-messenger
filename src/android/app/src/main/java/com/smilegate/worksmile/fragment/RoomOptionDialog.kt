package com.smilegate.worksmile.fragment

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.smilegate.worksmile.common.Constants
import com.smilegate.worksmile.common.PreferenceUtil
import com.smilegate.worksmile.model.chatroom.ChatRoom

class RoomOptionDialog : DialogFragment() {
    var itemClickListener: ItemClickListener? = null

    interface ItemClickListener {
        fun onSetFavoriteClicked(roomId: String, isFavorite: Boolean)
        fun onDeleteRoomClicked(roomId: String)
        fun onExitRoomClicked(roomId: String)
    }

    companion object {
        fun newInstance(room: ChatRoom): RoomOptionDialog {
            return RoomOptionDialog().apply {
                arguments = Bundle().apply {
                    putString(Constants.EXTRA_LEADER_ID, room.leader_id)
                    putString(Constants.EXTRA_ROOM_ID, room.roomId)
                    putString(Constants.EXTRA_ROOM_NAME, room.roomName)
                    putBoolean(Constants.EXTRA_FAVORITE, room.isFavorite)
                }
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val leaderId: String = arguments?.getString(Constants.EXTRA_LEADER_ID).orEmpty()
        val roomId: String = arguments?.getString(Constants.EXTRA_ROOM_ID).orEmpty()

        val currentIsFavorite: Boolean =
            arguments?.getBoolean(Constants.EXTRA_FAVORITE, false) ?: false

        val items = mutableListOf<String>().apply {
            add(if (currentIsFavorite) "즐겨찾기 제외" else "즐겨찾기 추가")
            add("채팅방에서 나가기")

            if (leaderId == PreferenceUtil.userId) {
                add("채팅방 삭제하기")
            }
        }.toTypedArray()

        return activity?.let {
            AlertDialog.Builder(it)
                .setTitle(arguments?.getString(Constants.EXTRA_ROOM_NAME, "").orEmpty())
                .setItems(
                    items
                ) { dialog, position ->
                    when (position) {
                        0 -> itemClickListener?.onSetFavoriteClicked(roomId, !currentIsFavorite)
                        1 -> itemClickListener?.onExitRoomClicked(roomId)
                        2 -> itemClickListener?.onDeleteRoomClicked(roomId)
                    }
                    dialog.dismiss()
                }.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}