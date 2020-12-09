package com.zhuinden.mvvmaacrxjavaretrofitroom.data

import com.zhuinden.mvvmaacrxjavaretrofitroom.data.local.dao.CatDao
import com.zhuinden.mvvmaacrxjavaretrofitroom.data.local.entity.Cat
import com.zhuinden.mvvmaacrxjavaretrofitroom.data.remote.api.CatBO
import com.zhuinden.mvvmaacrxjavaretrofitroom.data.remote.service.CatService
import com.zhuinden.mvvmaacrxjavaretrofitroom.utils.schedulers.ThreadScheduler
import io.reactivex.Single
import io.reactivex.rxkotlin.subscribeBy
import java.util.concurrent.atomic.AtomicBoolean

class GetCatsFetchTaskRunner(
    val catsService: CatService,
    val catDao: CatDao,
    val networkThreadScheduler: ThreadScheduler,
    val ioThreadScheduler: ThreadScheduler
) {
    private val isTaskRunning = AtomicBoolean(false)
    fun isTaskRunning(): Boolean = isTaskRunning.get()

    fun execute(): Single<Unit> = Single.create { emitter ->
        catsService.getCats()
            .doOnSubscribe { isTaskRunning.set(true) }
            .subscribeOn(networkThreadScheduler.asRxScheduler())
            .observeOn(ioThreadScheduler.asRxScheduler())
            .doFinally { isTaskRunning.set(false) }
            .subscribeBy(onSuccess = { catsBO ->
                val cats = catsBO.cats
                if (cats == null) {
                    emitter.onSuccess(Unit)
                    return@subscribeBy
                }
                val maxRank = catDao.count()
                cats.mapIndexed { index: Int, catBO: CatBO ->
                    with(catBO) {
                        Cat(id!!, url, sourceUrl, maxRank + index)
                    }
                }.let { catList ->
                    catDao.insertCats(catList)
                }
                emitter.onSuccess(Unit)
            }, onError = { throwable ->
                emitter.onError(throwable)
            })
    }
}

