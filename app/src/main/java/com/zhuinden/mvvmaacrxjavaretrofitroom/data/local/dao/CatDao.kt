package com.zhuinden.mvvmaacrxjavaretrofitroom.data.local.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.zhuinden.mvvmaacrxjavaretrofitroom.data.local.entity.Cat
import io.reactivex.Flowable

@Dao
interface CatDao {
    @Query("SELECT * FROM ${Cat.TABLE_NAME} ORDER BY ${Cat.COLUMN_RANK}")
    fun listenForCats(): Flowable<List<Cat>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCats(cats: List<Cat>)

    @Query("SELECT COUNT(*) FROM ${Cat.TABLE_NAME}")
    fun count(): Int
}