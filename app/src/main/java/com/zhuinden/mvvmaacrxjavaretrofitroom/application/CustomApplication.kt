package com.zhuinden.mvvmaacrxjavaretrofitroom.application

import android.app.Application
import androidx.room.Room
import com.zhuinden.mvvmaacrxjavaretrofitroom.data.CatDownloadManager
import com.zhuinden.mvvmaacrxjavaretrofitroom.data.GetCatsFetchTaskRunner
import com.zhuinden.mvvmaacrxjavaretrofitroom.data.local.dao.CatDao
import com.zhuinden.mvvmaacrxjavaretrofitroom.data.local.database.DatabaseManager
import com.zhuinden.mvvmaacrxjavaretrofitroom.data.remote.service.CatService
import com.zhuinden.mvvmaacrxjavaretrofitroom.utils.schedulers.IoThreadScheduler
import com.zhuinden.mvvmaacrxjavaretrofitroom.utils.schedulers.NetworkThreadScheduler
import com.zhuinden.mvvmaacrxjavaretrofitroom.utils.schedulers.ThreadScheduler
import com.zhuinden.mvvmaacrxjavaretrofitroom.utils.schedulers.UiThreadScheduler
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

class CustomApplication : Application() {
    private lateinit var databaseManager: DatabaseManager

    lateinit var catDao: CatDao
        private set

    lateinit var retrofit: Retrofit
        private set

    lateinit var catService: CatService
        private set

    lateinit var uiThreadScheduler: ThreadScheduler
        private set

    lateinit var networkThreadScheduler: ThreadScheduler
        private set

    lateinit var ioThreadScheduler: ThreadScheduler
        private set

    lateinit var catDownloadManager: CatDownloadManager
        private set

    lateinit var getCatsFetchTaskRunner: GetCatsFetchTaskRunner
        private set

    override fun onCreate() {
        super.onCreate()
        databaseManager = Room.databaseBuilder(this, DatabaseManager::class.java, "database")
            .fallbackToDestructiveMigration() //
            .allowMainThreadQueries() // TODO: count()
            .build();

        catDao = databaseManager.catDao
        retrofit = Retrofit.Builder().addConverterFactory(SimpleXmlConverterFactory.create())
            .baseUrl("http://thecatapi.com/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        catService = retrofit.create(CatService::class.java);

        uiThreadScheduler = UiThreadScheduler()
        ioThreadScheduler = IoThreadScheduler()
        networkThreadScheduler = NetworkThreadScheduler()

        getCatsFetchTaskRunner = GetCatsFetchTaskRunner(catService, catDao, networkThreadScheduler, ioThreadScheduler)

        catDownloadManager = CatDownloadManager(getCatsFetchTaskRunner, catDao)
    }
}
