package com.zhuinden.mvvmaacrxjavaretrofitroom.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.zhuinden.mvvmaacrxjavaretrofitroom.data.local.dao.CatDao
import com.zhuinden.mvvmaacrxjavaretrofitroom.data.local.entity.Cat


@Database(entities = [Cat::class], version = 1)
@TypeConverters(RoomTypeConverters::class)
abstract class DatabaseManager : RoomDatabase() {
    abstract val catDao: CatDao
}