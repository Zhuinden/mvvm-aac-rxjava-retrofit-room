package com.zhuinden.mvvmaacrxjavaretrofitroom.features.cats

import androidx.lifecycle.ViewModel
import com.zhuinden.eventemitter.EventEmitter
import com.zhuinden.eventemitter.EventSource
import com.zhuinden.mvvmaacrxjavaretrofitroom.data.CatDownloadManager
import com.zhuinden.mvvmaacrxjavaretrofitroom.data.local.dao.CatDao
import com.zhuinden.mvvmaacrxjavaretrofitroom.data.local.entity.Cat
import com.zhuinden.mvvmaacrxjavaretrofitroom.utils.schedulers.ThreadScheduler
import io.reactivex.Flowable

class CatViewModel(
    val catDao: CatDao,
    val catDownloadManager: CatDownloadManager,
    val uiThreadScheduler: ThreadScheduler
) : ViewModel() {
    val cats: Flowable<List<Cat>> = catDao.getCatsWithChanges()
        .observeOn(uiThreadScheduler.asRxScheduler())
        .replay(1)
        .autoConnect(0)

    private val openCatEventEmitter = EventEmitter<Cat>()
    val openCatEvent: EventSource<Cat> = openCatEventEmitter

    fun handleScrollToBottom(scrolled: Boolean) {
        catDownloadManager.startFetch(scrolled)
    }

    fun openCatPage(cat: Cat) {
        openCatEventEmitter.emit(cat)
    }
}