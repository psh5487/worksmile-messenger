package com.smilegate.worksmile.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smilegate.worksmile.R
import com.smilegate.worksmile.activity.LoginActivity
import com.smilegate.worksmile.activity.SignUpActivity
import com.smilegate.worksmile.common.PreferenceUtil
import com.smilegate.worksmile.model.auth.LogoutRequest
import com.smilegate.worksmile.network.ApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_setting.*

class SettingFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        cv_logout.setOnClickListener {
            ApiService.loginApi.getLogoutReq(
                PreferenceUtil.userToken,
                LogoutRequest(PreferenceUtil.userId)
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        showToast(it.message)
                        requireActivity().finish()
                        startActivity(Intent(requireContext(), LoginActivity::class.java))
                    }, {
                        showToast(it.message.toString())
                    }
                )
        }
    }
}