package com.zhuinden.mvvmaacrxjavaretrofitroom.utils.schedulers

import android.os.Handler
import android.os.Looper
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.Executor

class MainScheduler : Scheduler {
    override fun asRxScheduler(): io.reactivex.Scheduler = AndroidSchedulers.mainThread()

    private val handler: Handler = Handler(Looper.getMainLooper())

    override fun runOnThread(runnable: ExecutionBlock) {
        if(Looper.myLooper() == Looper.getMainLooper()) {
            runnable()
        } else {
            handler.post(runnable)
        }
    }
}