package com.zhuinden.mvvmaacrxjavaretrofitroom.data.remote.task

import android.util.Log
import com.zhuinden.mvvmaacrxjavaretrofitroom.data.local.dao.CatDao
import com.zhuinden.mvvmaacrxjavaretrofitroom.data.local.entity.Cat
import com.zhuinden.mvvmaacrxjavaretrofitroom.data.remote.api.CatBO
import com.zhuinden.mvvmaacrxjavaretrofitroom.data.remote.service.CatService
import com.zhuinden.mvvmaacrxjavaretrofitroom.data.remote.task.CatTaskManager.Companion.SAVE_CATS_TASK
import com.zhuinden.mvvmaacrxjavaretrofitroom.utils.schedulers.Scheduler
import io.reactivex.Single
import io.reactivex.rxkotlin.subscribeBy

class CatTaskFactory(
    val catsTaskManager: CatTaskManager,
    val catsService: CatService,
    val catDao: CatDao,
    val networkScheduler: Scheduler,
    val ioScheduler: Scheduler
) {
    fun createSaveCatsTask(): Single<Unit> = Single.create { emitter ->
        catsTaskManager.registerTask(SAVE_CATS_TASK)
        catsService.getCats()
            .subscribeOn(networkScheduler.asRxScheduler())
            .observeOn(ioScheduler.asRxScheduler())
            .doFinally { catsTaskManager.unregisterTask(SAVE_CATS_TASK) }
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
                    Log.i(SAVE_CATS_TASK, "Saving cats to database.");
                    catDao.insertCats(catList)
                }
                emitter.onSuccess(Unit)
            }, onError = { throwable ->
                emitter.onError(throwable)
            })
    }
}