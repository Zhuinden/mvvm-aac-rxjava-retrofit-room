package com.zhuinden.mvvmaacrxjavaretrofitroom.features.cats

import android.arch.lifecycle.ViewModel
import android.util.Log
import com.zhuinden.mvvmaacrxjavaretrofitroom.data.CatRepository
import com.zhuinden.mvvmaacrxjavaretrofitroom.data.local.dao.CatDao
import com.zhuinden.mvvmaacrxjavaretrofitroom.data.local.entity.Cat
import com.zhuinden.mvvmaacrxjavaretrofitroom.data.remote.task.CatTaskFactory
import com.zhuinden.mvvmaacrxjavaretrofitroom.data.remote.task.CatTaskManager
import com.zhuinden.mvvmaacrxjavaretrofitroom.utils.schedulers.Scheduler
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy

class CatViewModel(
    val catRepository: CatRepository
) : ViewModel() {
    val cats: Flowable<List<Cat>> = catRepository.startListeningForCats()

    fun observeCats(onNext: (List<Cat>) -> Unit, onError: (Throwable) -> Unit = {}): Disposable =
        cats.subscribeBy(onNext = onNext, onError = onError)

    fun handleScrollToBottom(scrolled: Boolean) {
        catRepository.startFetch(scrolled)
    }
}