package com.smilegate.worksmile.activity

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import com.smilegate.worksmile.R
import com.smilegate.worksmile.common.Constants
import com.smilegate.worksmile.common.PreferenceUtil
import com.smilegate.worksmile.model.auth.LoginRequest
import com.smilegate.worksmile.network.ApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val userId = PreferenceUtil.userId
        val userPw = PreferenceUtil.userPassword

        if (userId.isNotBlank() && userPw.isNotBlank()) {
            login(userId, userPw) {
                if (!it) {
                    initView()
                }
            }
        } else {
            initView()
        }
    }

    private fun initView() {
        et_pwd_login.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                login()
            }
            true
        }

        btn_login.setOnClickListener {
            login()
        }

        btn_signup.setOnClickListener {
            val signUpIntent = Intent(this, SignUpActivity::class.java)
            startActivity(signUpIntent)
        }
    }

    private fun login() {
        login(et_id_login.text.toString(), et_pwd_login.text.toString())
    }

    private fun login(
        userId: String,
        password: String,
        result: ((Boolean) -> Unit)? = null
    ) {
        when {
            userId.isBlank() -> {
                showToast("아이디를 입력해주세요")
                et_id_login.requestFocus()
            }
            password.isBlank() -> {
                showToast("비밀번호를 입력해주세요")
                et_pwd_login.requestFocus()
            }
            else -> {
                ApiService.loginApi.getLoginRes(LoginRequest(userId, password))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        {
                            if (it.status == 200) {
                                PreferenceUtil.setString(Constants.KEY_USER_ID, userId)
                                PreferenceUtil.setString(Constants.KEY_USER_PW, password)
                                PreferenceUtil.setString(Constants.KEY_USER_TOKEN, it.data.token)

                                PreferenceUtil.setString(
                                    Constants.EXTRA_USER_NAME,
                                    it.data.user.uname
                                )

                                startActivity(Intent(this, HomeActivity::class.java))
                                finish()
                            } else {
                                result?.run {
                                    invoke(false)
                                } ?: showToast(it.message)
                            }
                        }, {
                        }
                    )
            }
        }
    }
}