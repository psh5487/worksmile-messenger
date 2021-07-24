package com.smilegate.worksmile.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.smilegate.worksmile.R

class ChatMenuBottomSheetFragment : BottomSheetDialogFragment() {
    var itemClickListener: ItemClickListener? = null

    interface ItemClickListener {
        fun onAttachImageClicked()
        fun onAttachFileClicked()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat_menu_bottomsheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.menu_attach_image).setOnClickListener {
            dismiss()
            itemClickListener?.onAttachImageClicked()
        }

        view.findViewById<TextView>(R.id.menu_attach_file).setOnClickListener {
            dismiss()
            itemClickListener?.onAttachFileClicked()
        }
    }
}