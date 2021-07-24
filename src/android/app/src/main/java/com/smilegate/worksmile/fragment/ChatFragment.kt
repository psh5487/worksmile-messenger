package com.smilegate.worksmile.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.smilegate.worksmile.R
import com.smilegate.worksmile.adapter.chat.MessageAdapter
import com.smilegate.worksmile.adapter.listener.MessageClickListener
import com.smilegate.worksmile.common.CommonUtils
import com.smilegate.worksmile.common.Constants
import com.smilegate.worksmile.common.PreferenceUtil
import com.smilegate.worksmile.extensions.setVisibility
import com.smilegate.worksmile.model.chat.MessageType
import com.smilegate.worksmile.model.UserIdList
import com.smilegate.worksmile.model.chat.ChatMessage
import com.smilegate.worksmile.model.chatroom.LastMessageIndex
import com.smilegate.worksmile.network.ApiService
import com.smilegate.worksmile.viewmodel.ChatViewModel
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.android.synthetic.main.nav_drawer_item.*
import ua.naiksoftware.stomp.dto.LifecycleEvent
import java.util.*

class ChatFragment : BaseFragment(), TextWatcher {
    private val mViewModel: ChatViewModel by activityViewModels()

    private var mSenderId: String = ""
    private var mRoomId: String = ""
    private var mUserName: String = ""

    private var mParentMessageId: Long? = null

    private val mMessageAdapter: MessageAdapter by lazy {
        MessageAdapter().apply {
            itemClickListener = object : MessageClickListener {
                @SuppressLint("SetTextI18n")
                override fun onMessageLongClicked(message: ChatMessage) {
                    val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
                    val items = mutableListOf<String>().apply {
                        add("공지 등록")

                        if (message.sender != PreferenceUtil.userId) {
                            add("답장 하기")
                        }
                    }.toTypedArray()

                    builder.setItems(items) { _, position ->
                        when (position) {
                            0 -> registerNotify(message)
                            1 -> {
                                mParentMessageId = message.midx

                                showReplyLayout(true)

                                et_reply_chat.run {
                                    text = "${message.uname}님에게 답장\n${message.content}"
                                    visibility = View.VISIBLE
                                }

                                et_send_chat.requestFocus()
                            }
                        }
                    }
                    builder.show()
                }

                override fun onImageClicked(imageUrl: String) {
                    startActivity(Intent(Intent.ACTION_VIEW).setData(Uri.parse(imageUrl)))
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mRoomId = arguments?.getString(Constants.EXTRA_ROOM_ID, "") ?: ""
        mSenderId = arguments?.getString(Constants.EXTRA_USER_ID, "") ?: ""
        mUserName = arguments?.getString(Constants.EXTRA_USER_NAME, "") ?: ""


        if (mSenderId.isBlank() || mRoomId.isBlank()) {
            requireActivity().finish()
            return
        }

        initView()
        initViewModel(arguments?.getStringArray(Constants.EXTRA_INVITED_USERS))
    }

    private fun initView() {
        replay_close.setOnClickListener {
            showReplyLayout(false)
        }

        rv_message_chat.run {
            adapter = mMessageAdapter
            layoutManager = LinearLayoutManager(context)
        }

        et_send_chat.run {
            addTextChangedListener(this@ChatFragment)

            setOnEditorActionListener { _, i, _ ->
                when (i) {
                    EditorInfo.IME_ACTION_SEND -> {
                        sendMessage()
                        true
                    }
                    else -> false
                }
            }
        }

        btn_add_chat.setOnClickListener {
            ChatMenuBottomSheetFragment().apply {
                itemClickListener = object : ChatMenuBottomSheetFragment.ItemClickListener {
                    override fun onAttachImageClicked() {
                        CommonUtils.requestPermission(
                            requireActivity(),
                            arrayOf(
                                Manifest.permission.READ_EXTERNAL_STORAGE
                            ),
                            Constants.REQ_READ_STORAGE_PERMISSION
                        ) {
                            pickPicture()
                        }
                    }

                    override fun onAttachFileClicked() {
                        CommonUtils.requestPermission(
                            requireActivity(),
                            arrayOf(
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                            ),
                            Constants.REQ_READ_STORAGE_PERMISSION
                        ) {
                            pickFile()
                        }
                    }
                }
            }.show(childFragmentManager, "chatMenu")
        }

        iv_noticeFold_chat.setOnClickListener {
            cl_notice_chat.setVisibility(false)
        }

        // 메시지 전송 클릭시
        btn_send_chat.setOnClickListener {
            sendMessage()
        }

        keyword_direction_down.setOnClickListener {
            mViewModel.moveToNextSearchMessage()
        }

        keyword_direction_up.setOnClickListener {
            mViewModel.moveToPreviousSearchMessage()
        }
    }

    @SuppressLint("CheckResult")
    fun initViewModel(invitedUsers: Array<String>? = null) {
        mViewModel.run {
            stompEvent.observe(viewLifecycleOwner, { event ->
                when (event.type) {
                    LifecycleEvent.Type.OPENED -> {
                        invitedUsers.takeIf { !it.isNullOrEmpty() }?.let {
                            sendMessage(
                                mSenderId,
                                mRoomId,
                                content = UserIdList(it.toList()),
                                type = MessageType.ENTER
                            )
                        }

                        sendMessage(
                            mSenderId,
                            mRoomId,
                            content = "",
                            type = MessageType.ON
                        )
                    }
                    else -> {
                    }
                }
            })

            roomMessageList.observe(viewLifecycleOwner, {
                mMessageAdapter.setChatMessageList(it)
                moveToLastMessage()
            })

            roomMessage.observe(viewLifecycleOwner, {
                mMessageAdapter.addChatMessage(it)
                moveToLastMessage()
                requireActivity().setResult(Activity.RESULT_OK)
            })

            message.observe(viewLifecycleOwner, {
                showToast(it)
            })

            messageSearchMode.observe(viewLifecycleOwner, {
                cv_send_chat.setVisibility(!it)
                keyword_direction_layout.setVisibility(it)
            })

            keywordMessageId.observe(viewLifecycleOwner, { messageId ->
                Single.fromCallable {
                    mMessageAdapter.messageList.indexOfFirst { (it as? ChatMessage)?.midx == messageId }
                }.filter { it >= 0 }.subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe({
                        rv_message_chat.scrollToPosition(it)
                    }, {})
            })

            notice.observe(viewLifecycleOwner, {
                if (!it.isNullOrBlank()) {
                    iv_notice_chat.text = it
                    cl_notice_chat.setVisibility(true)
                    showToast("공지로 등록되었습니다.")
                }
            })

            getPreviousMessages(mRoomId)
        }
    }

    private fun pickPicture() {
        startActivityForResult(Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
            putExtra(
                Intent.EXTRA_MIME_TYPES,
                arrayOf("image/jpeg", "image/png")
            )
        }, Constants.REQ_PICK_PICTURE)
    }

    private fun pickFile() {
        startActivityForResult(Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = "*/*"
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            putExtra(Intent.EXTRA_LOCAL_ONLY, true)
        }, Constants.REQ_PICK_FILE)
    }

    @SuppressLint("SetTextI18n")
    private fun registerNotify(message: ChatMessage) {
        mViewModel.sendMessage(
            mSenderId,
            mRoomId,
            message.content,
            MessageType.NOTIFY
        )

        reply_layout.setVisibility(false)
    }

    private fun sendMessage() {
        mViewModel.sendMessage(
            mSenderId,
            mRoomId,
            et_send_chat.text.toString(),
            if (mParentMessageId == null) MessageType.TALK else MessageType.REPLY,
            mParentMessageId
        )

        showReplyLayout(false)
        resetMessageEdit()
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
    }

    override fun afterTextChanged(s: Editable) {
        val string = s.toString().trim()
        if (string.isEmpty()) {
            resetMessageEdit()
        } else {
            btn_send_chat.visibility = View.VISIBLE
        }
    }

    private fun showReplyLayout(isShown: Boolean) {
        reply_layout.setVisibility(isShown)
        reply_icon.setVisibility(isShown)
        btn_add_chat.visibility = if (isShown) View.INVISIBLE else View.VISIBLE

        if (!isShown) {
            mParentMessageId = null
        }
    }

    private fun resetMessageEdit() {
        et_send_chat.removeTextChangedListener(this)
        et_send_chat.setText("")
        btn_send_chat.visibility = View.GONE
        et_send_chat.addTextChangedListener(this)
    }

    private fun moveToLastMessage() {
        (rv_message_chat.layoutManager as? LinearLayoutManager)?.scrollToPositionWithOffset(
            mMessageAdapter.itemCount - 1, 0
        )
    }

    @SuppressLint("CheckResult")
    override fun onStop() {
        super.onStop()

        mViewModel.sendMessage(
            mSenderId,
            mRoomId,
            content = "",
            type = MessageType.OFF
        )

        ApiService.roomApi.refreshLastMessageIndex(
            LastMessageIndex(mSenderId, mRoomId, mMessageAdapter.lastMessageIndex)
        ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({}, {})
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                Constants.REQ_PICK_PICTURE -> {
                    data?.data?.let {
                        mViewModel.uploadFile(mRoomId, "IMAGE", it)
                    }
                }
                Constants.REQ_PICK_FILE -> {
                    data?.data?.let {
                        mViewModel.uploadFile(mRoomId, "FILE", it)
                    }
                }
                else -> super.onActivityResult(requestCode, resultCode, data)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
