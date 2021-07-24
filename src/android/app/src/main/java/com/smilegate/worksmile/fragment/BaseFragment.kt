package com.smilegate.worksmile.fragment

import android.widget.Toast
import androidx.fragment.app.Fragment
import com.smilegate.worksmile.common.Constants
import com.smilegate.worksmile.common.PreferenceUtil

open class BaseFragment : Fragment() {
    private var mToast: Toast? = null

    fun showToast(message: String) {
        if (message.isNotBlank()) {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
//            (mToast ?: Toast(requireContext()).also { mToast = it }).run {
//                duration = Toast.LENGTH_SHORT
//                setText(message)
//                show()
//            }
        }
    }
}