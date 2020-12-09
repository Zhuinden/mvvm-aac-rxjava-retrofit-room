package com.zhuinden.mvvmaacrxjavaretrofitroom.utils.schedulers

import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executors

class NetworkThreadScheduler : ThreadScheduler {
    override fun asRxScheduler(): io.reactivex.Scheduler = Schedulers.from(executor)

    private val executor = Executors.newFixedThreadPool(2)

    override fun runOnThread(runnable: ExecutionBlock) {
        executor.execute(runnable)
    }
}