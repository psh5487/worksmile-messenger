package com.smilegate.worksmile.activity

import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.smilegate.worksmile.R

open class BaseActivity : AppCompatActivity() {

    companion object {
        const val TAG = "SYH"
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)

        findViewById<Toolbar>(R.id.toolbar)?.let {
            setSupportActionBar(it)
        }

        supportActionBar?.run {
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun showToast(message: String) {
        if (message.isNotBlank()) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    fun throwError(msg: Throwable) {
        msg.message?.let { Log.e(TAG, it) }
    }
}