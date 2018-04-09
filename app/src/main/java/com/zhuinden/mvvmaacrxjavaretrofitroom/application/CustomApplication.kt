package com.zhuinden.mvvmaacrxjavaretrofitroom.application

import android.app.Application
import android.arch.persistence.room.Room
import com.zhuinden.mvvmaacrxjavaretrofitroom.data.CatRepository
import com.zhuinden.mvvmaacrxjavaretrofitroom.data.local.dao.CatDao
import com.zhuinden.mvvmaacrxjavaretrofitroom.data.local.database.DatabaseManager
import com.zhuinden.mvvmaacrxjavaretrofitroom.data.remote.service.CatService
import com.zhuinden.mvvmaacrxjavaretrofitroom.data.remote.task.CatTaskFactory
import com.zhuinden.mvvmaacrxjavaretrofitroom.data.remote.task.CatTaskManager
import com.zhuinden.mvvmaacrxjavaretrofitroom.utils.schedulers.IoScheduler
import com.zhuinden.mvvmaacrxjavaretrofitroom.utils.schedulers.MainScheduler
import com.zhuinden.mvvmaacrxjavaretrofitroom.utils.schedulers.NetworkScheduler
import com.zhuinden.mvvmaacrxjavaretrofitroom.utils.schedulers.Scheduler
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

    lateinit var mainScheduler: Scheduler
        private set

    lateinit var networkScheduler: Scheduler
        private set

    lateinit var ioScheduler: Scheduler
        private set

    lateinit var catTaskFactory: CatTaskFactory
        private set

    lateinit var catTaskManager: CatTaskManager
        private set

    lateinit var catRepository: CatRepository
        private set

    override fun onCreate() {
        super.onCreate()
        databaseManager = Room.databaseBuilder(this, DatabaseManager::class.java, "database")
            .fallbackToDestructiveMigration() //
            .allowMainThreadQueries() // TODO: count()
            .build();

        catDao = databaseManager.catDao
        retrofit = Retrofit.Builder().addConverterFactory(SimpleXmlConverterFactory.create())
            .baseUrl("http://thecatapi.com/").addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        catService = retrofit.create(CatService::class.java);

        mainScheduler = MainScheduler()
        ioScheduler = IoScheduler()
        networkScheduler = NetworkScheduler()

        catTaskManager = CatTaskManager()
        catTaskFactory = CatTaskFactory(catTaskManager, catService, catDao, networkScheduler, ioScheduler)

        catRepository = CatRepository(catDao, catTaskManager, catTaskFactory, mainScheduler)
    }
}