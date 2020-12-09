package com.zhuinden.mvvmaacrxjavaretrofitroom.data.local.database

import androidx.room.TypeConverter
import java.util.*

object RoomTypeConverters {
    @JvmStatic
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @JvmStatic
    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return if (date == null) null else date.time.toLong()
    }
}