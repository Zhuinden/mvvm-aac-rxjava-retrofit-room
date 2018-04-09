package com.zhuinden.mvvmaacrxjavaretrofitroom.features.cats

import android.arch.lifecycle.ViewModel
import com.jakewharton.rxrelay2.PublishRelay
import com.zhuinden.mvvmaacrxjavaretrofitroom.data.CatRepository
import com.zhuinden.mvvmaacrxjavaretrofitroom.data.local.entity.Cat
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy

class CatViewModel(
    val catRepository: CatRepository
) : ViewModel() {
    val cats: Flowable<List<Cat>> = catRepository.startListeningForCats()

    val openCatEvent = PublishRelay.create<Cat>()

    fun observeCats(onNext: (List<Cat>) -> Unit, onError: (Throwable) -> Unit = {}): Disposable =
        cats.subscribeBy(onNext = onNext, onError = onError)

    fun handleScrollToBottom(scrolled: Boolean) {
        catRepository.startFetch(scrolled)
    }

    fun openCatPage(cat: Cat) {
        openCatEvent.accept(cat)
    }
}