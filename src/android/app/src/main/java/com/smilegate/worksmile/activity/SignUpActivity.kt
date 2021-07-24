package com.smilegate.worksmile.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.smilegate.worksmile.R
import com.smilegate.worksmile.model.UserId
import com.smilegate.worksmile.model.auth.SignUpRequest
import com.smilegate.worksmile.network.ApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : BaseActivity() {

    lateinit var companyArray: Array<String>

    private var subRootCid = 0
    private var isUnique = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        loadCompanyList()
        initView()
    }

    @SuppressLint("CheckResult")
    private fun loadCompanyList() {
        ApiService.userApi.getCompanyList()
            .map { it.getDataOrThrowable() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    companyArray = Array(it.size) { "" }
                    for ((index, company) in it.withIndex()) {
                        companyArray[index] = company.cname
                    }
                },
                {
                    throwError(it)
                }
            )
    }


    @SuppressLint("CheckResult")
    private fun initView() {
        btn_idCheck_signup.setOnClickListener {
            idCheck()
        }

        btn_signup_login.setOnClickListener {
            signUp()
        }

        btn_company_signup.setOnClickListener {
            showCompanyList()
        }
    }

    @SuppressLint("CheckResult")
    private fun idCheck() {
        ApiService.signUpApi.isUniqueReq(UserId(et_id_signup.text.toString()))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    showToast(it.message)
                    isUnique = true
                }, {
                    showToast(it.message.toString())
                    Log.d(TAG, "IsUnique thorw ${it.message}")
                }
            )
    }

    private fun signUp() {
        val uid = et_id_signup.text.toString()
        val password = et_pwd_signup.text.toString()
        val confirm = et_confirm_signup.text.toString()
        val name = et_name_signup.text.toString()
        val phone = et_phone_signup.text.toString()
        val email = et_email_signup.text.toString()


        when {
            uid.isBlank() -> {
                showToast("아이디를 입력해주세요")
                et_id_login.requestFocus()
            }
            password.isBlank() -> {
                showToast("비밀번호를 입력해주세요")
                et_pwd_login.requestFocus()
            }
            confirm.isBlank() -> {
                showToast("비밀번호 확인을 입력해주세요")
                et_confirm_signup.requestFocus()
            }
            name.isBlank() -> {
                showToast("이름 확인을 입력해주세요")
                et_name_signup.requestFocus()
            }
            phone.isBlank() -> {
                showToast("핸드폰 번호를 확인을 입력해주세요")
                et_phone_signup.requestFocus()
            }
            email.isBlank() -> {
                showToast("이메일 주소를 확인해 주세요")
            }
            confirm != password -> {
                showToast("패스워드가 일치하지 않습니다")
            }
            else -> {
                ApiService.signUpApi.getSignUpRes(
                    req = SignUpRequest(
                        subroot_cid = subRootCid,
                        subroot_cname = companyArray[1],
                        email = et_email_signup.text.toString(),
                        phone = et_phone_signup.text.toString(),
                        pwd = et_phone_signup.text.toString(),
                        type = "userRegister",
                        uid = et_id_signup.text.toString(),
                        uname = et_name_signup.text.toString()
                    )
                ).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        {
                            showToast(it.message)
                        }, {
                            showToast(it.message.toString())
                        }
                    )
            }
        }
    }

    private fun showCompanyList() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setItems(companyArray) { _, position ->
            when (position) {
                0 -> {
                    subRootCid = 2
                    btn_company_signup.text = companyArray[position]
                }
                1 -> {
                    subRootCid = 3
                    btn_company_signup.text = companyArray[position]
                }
                2 -> {
                    subRootCid = 4
                    btn_company_signup.text = companyArray[position]
                }
            }
        }
        builder.show()
    }
}