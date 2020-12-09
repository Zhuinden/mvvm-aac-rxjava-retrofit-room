package com.zhuinden.mvvmaacrxjavaretrofitroom.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zhuinden.mvvmaacrxjavaretrofitroom.data.local.entity.Cat
import io.reactivex.Flowable

@Dao
interface CatDao {
    @Query("SELECT * FROM ${Cat.TABLE_NAME} ORDER BY ${Cat.COLUMN_RANK}")
    fun getCatsWithChanges(): Flowable<List<Cat>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCats(cats: List<Cat>)

    @Query("SELECT COUNT(*) FROM ${Cat.TABLE_NAME}")
    fun count(): Int
}