package com.smilegate.worksmile.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.smilegate.worksmile.common.PreferenceUtil
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BaseViewModel(application: Application) : AndroidViewModel(application) {
    protected val mMessage = MutableLiveData<String>()
    val mUserId = PreferenceUtil.userId

    private val mDisposable = CompositeDisposable()

    protected val context: Context
        get() = getApplication()

    val message: LiveData<String> = mMessage

    fun Disposable.addDisposable() {
        mDisposable.add(this)
    }

    override fun onCleared() {
        if (!mDisposable.isDisposed) {
            mDisposable.dispose()
        }

        super.onCleared()
    }
}