package com.zhuinden.mvvmaacrxjavaretrofitroom.data.local.database

import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.arch.persistence.room.Database
import com.zhuinden.mvvmaacrxjavaretrofitroom.data.local.dao.CatDao
import com.zhuinden.mvvmaacrxjavaretrofitroom.data.local.entity.Cat


@Database(entities = [Cat::class], version = 1)
@TypeConverters(RoomTypeConverters::class)
abstract class DatabaseManager : RoomDatabase() {
    abstract val catDao: CatDao
}