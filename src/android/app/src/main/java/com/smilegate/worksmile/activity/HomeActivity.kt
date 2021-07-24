package com.smilegate.worksmile.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.smilegate.worksmile.*
import com.smilegate.worksmile.common.Constants
import com.smilegate.worksmile.common.PreferenceUtil
import com.smilegate.worksmile.fragment.*
import kotlinx.android.synthetic.main.activity_main.*


class HomeActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    private fun init() {
        bottom_nav_main.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_message -> {
                    setFragment(RoomListFragment(), "메세지")
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_organization -> {
                    setFragment(OrganizationFragment(), "조직도")
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_calender -> {
                    setFragment(CalenderFragment(), "일정")
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.nav_setting -> {
                    setFragment(SettingFragment(), "설정")
                    return@setOnNavigationItemSelectedListener true
                }
                else -> {
                    return@setOnNavigationItemSelectedListener true
                }
            }
        }
        setFragment(RoomListFragment(), "메세지")

        val items = arrayOf("사용자 검색", "채널 검색")

        iv_search_main.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setTitle("검색 설정")
            builder.setItems(items) { _, position ->
                when (position) {
                    0 -> {
                        val intent = Intent(this, GeneralSearchActivity::class.java)
                        startActivity(intent)
                    }
                    1 -> {
                        val intent = Intent(this, OpenChatSearchActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
            builder.show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        return true
    }

    private fun setFragment(fragment: Fragment, name: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_main, fragment).commit()
        tv_name_toolbar_main.text = name

    }
}