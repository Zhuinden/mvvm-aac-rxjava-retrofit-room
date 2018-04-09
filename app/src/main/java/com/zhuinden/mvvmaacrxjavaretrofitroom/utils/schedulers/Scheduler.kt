package com.zhuinden.mvvmaacrxjavaretrofitroom.utils.schedulers

import io.reactivex.Scheduler

typealias ExecutionBlock = () -> Unit

interface Scheduler {
    fun asRxScheduler(): Scheduler

    fun runOnThread(runnable: ExecutionBlock)
}