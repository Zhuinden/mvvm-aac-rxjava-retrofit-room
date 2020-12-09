package com.zhuinden.mvvmaacrxjavaretrofitroom.data.local.entity


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.zhuinden.mvvmaacrxjavaretrofitroom.data.local.entity.Cat.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class Cat(
    @PrimaryKey val id: String,
    val url: String?,
    val sourceUrl: String?,
    val rank: Int
) {
    companion object {
        const val TABLE_NAME = "CAT"

        const val COLUMN_ID = "id"
        const val COLUMN_URL = "url"
        const val COLUMN_SOURCE_URL = "source_url"
        const val COLUMN_RANK = "rank"
    }
}