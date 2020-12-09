package com.zhuinden.mvvmaacrxjavaretrofitroom.data

import android.annotation.SuppressLint
import android.util.Log
import com.zhuinden.mvvmaacrxjavaretrofitroom.data.local.dao.CatDao
import io.reactivex.rxkotlin.subscribeBy

class CatDownloadManager(
    private val getCatsFetchTaskRunner: GetCatsFetchTaskRunner,
    private val catDao: CatDao
) {
    fun isNoDataDownloaded(): Boolean = catDao.count() == 0 /* TODO: main thread query */

    @SuppressLint("CheckResult")
    fun startFetch(scrolled: Boolean) {
        if (!getCatsFetchTaskRunner.isTaskRunning() && (scrolled || isNoDataDownloaded())) {
            getCatsFetchTaskRunner.execute()
                .subscribeBy(onSuccess = {
                    // Success
                }, onError = { throwable ->
                    Log.i("CatDownloadManager", "Fetching failed", throwable)
                })
        }
    }
}